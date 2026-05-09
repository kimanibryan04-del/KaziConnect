package com.kim.kaziconnect.ui.screens.auth

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.kim.kaziconnect.navigation.ROUT_LOGIN
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    navController: NavHostController
) {

    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    var email by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val auth = FirebaseAuth.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lightBg)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .imePadding()
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 4.dp
            ) {

                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = colorPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Forgot Password",
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                color = colorPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Enter your email and we’ll send you a password reset link.",
                fontSize = 15.sp,
                color = Color.Gray,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(

                value = email,

                onValueChange = {
                    email = it
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(18.dp),

                singleLine = true,

                leadingIcon = {

                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = colorAccent
                    )
                },

                label = {
                    Text("Email Address")
                },

                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorAccent,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = colorAccent,
                    cursorColor = colorAccent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(

                onClick = {

                    if (email.isBlank()) {

                        Toast.makeText(
                            navController.context,
                            "Enter your email",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                        Toast.makeText(
                            navController.context,
                            "Enter a valid email",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

                    isLoading = true

                    auth.sendPasswordResetEmail(email)

                        .addOnCompleteListener { task ->

                            isLoading = false

                            if (task.isSuccessful) {

                                Toast.makeText(
                                    navController.context,
                                    "Password reset link sent to your email",
                                    Toast.LENGTH_LONG
                                ).show()

                                navController.navigate(ROUT_LOGIN) {
                                    popUpTo(0)
                                }

                            } else {

                                Toast.makeText(
                                    navController.context,
                                    task.exception?.message
                                        ?: "Something went wrong",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),

                shape = RoundedCornerShape(18.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = colorAccent
                )
            ) {

                if (isLoading) {

                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(22.dp)
                    )

                } else {

                    Text(
                        text = "Send Reset Link",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            TextButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {

                Text(
                    text = "Back to Login",
                    color = colorAccent,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {

    ForgotPasswordScreen(
        rememberNavController()
    )
}