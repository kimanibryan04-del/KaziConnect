package com.kim.kaziconnect.data

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kim.kaziconnect.models.User
import com.kim.kaziconnect.navigation.ROUT_CLIENTHOME
import com.kim.kaziconnect.navigation.ROUT_FUNDIHOME
import com.kim.kaziconnect.navigation.ROUT_LOGIN
import com.kim.kaziconnect.navigation.ROUT_REGISTER
import com.kim.kaziconnect.navigation.ROUT_ROLESELECTION // Ensure this exists in your routes

class AuthViewModel(val navController: NavController, val context: Context) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().getReference("Users")

    // Observed by the UI to show ProgressIndicators
    var isLoading = mutableStateOf(false)

    /**
     * 1. SIGNUP: Creates account with a blank role.
     * The role is selected in the next screen.
     */
    fun signUp(name: String, email: String, pass: String, confirmpass: String) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass != confirmpass) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        isLoading.value = true
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser?.uid ?: ""
                // Initialize user with an empty role to be filled in Step 2
                val userData = User(uid = uid, name = name, email = email, role = "")

                dbRef.child(uid).setValue(userData).addOnCompleteListener { dbTask ->
                    isLoading.value = false
                    if (dbTask.isSuccessful) {
                        Toast.makeText(context, "Account Created!", Toast.LENGTH_SHORT).show()
                        navController.navigate(ROUT_ROLESELECTION)
                    } else {
                        Toast.makeText(context, "DB Error: ${dbTask.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                isLoading.value = false
                Toast.makeText(context, "Auth Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * 2. ROLE UPDATE: Called from the RoleSelectionScreen.
     */
    fun updateUserRole(role: String) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            isLoading.value = true
            dbRef.child(uid).child("role").setValue(role).addOnCompleteListener { task ->
                isLoading.value = false
                if (task.isSuccessful) {
                    Toast.makeText(context, "Welcome, $role!", Toast.LENGTH_SHORT).show()
                    navigateBasedOnRole(role)
                } else {
                    Toast.makeText(context, "Update Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * 3. LOGIN: Checks the database for the role before navigating.
     */
    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        isLoading.value = true
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = auth.currentUser?.uid ?: ""

                // Fetch role to decide where to send the user
                dbRef.child(uid).child("role").get().addOnSuccessListener { snapshot ->
                    isLoading.value = false
                    val role = snapshot.value?.toString() ?: ""

                    when (role) {
                        "fundi" -> navController.navigate(ROUT_FUNDIHOME)
                        "client" -> navController.navigate(ROUT_CLIENTHOME)
                        else -> navController.navigate(ROUT_ROLESELECTION)
                    }
                }.addOnFailureListener {
                    isLoading.value = false
                    Toast.makeText(context, "Error fetching profile", Toast.LENGTH_SHORT).show()
                }
            } else {
                isLoading.value = false
                Toast.makeText(context, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * HELPER: Navigation Logic
     */
    private fun navigateBasedOnRole(role: String) {
        if (role == "fundi") {
            navController.navigate(ROUT_FUNDIHOME)
        } else {
            navController.navigate(ROUT_CLIENTHOME)
        }
    }

    fun logout() {
        auth.signOut()
        navController.navigate(ROUT_LOGIN)
    }

    fun isLoggedIn(): Boolean = auth.currentUser != null
}