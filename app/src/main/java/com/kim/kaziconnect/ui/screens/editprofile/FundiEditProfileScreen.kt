package com.kim.kaziconnect.ui.screens.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import androidx.compose.foundation.layout.imePadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundiEditProfileScreen(navController: NavController) {

    val context = LocalContext.current

    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    val userId =
        FirebaseAuth.getInstance().currentUser?.uid ?: ""

    var fullName by remember {
        mutableStateOf("")
    }

    var phone by remember {
        mutableStateOf("")
    }

    var location by remember {
        mutableStateOf("")
    }

    var skill by remember {
        mutableStateOf("")
    }

    var bio by remember {
        mutableStateOf("")
    }

    var experience by remember {
        mutableStateOf("")
    }

    var profileImage by remember {
        mutableStateOf("")
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->

        if (uri != null) {

            imageUri = uri

            // TEMPORARY UNTIL CLOUDINARY STORAGE
            profileImage = uri.toString()
        }
    }

    LaunchedEffect(Unit) {

        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(userId)
            .get()
            .addOnSuccessListener { snapshot ->

                fullName =
                    snapshot.child("fullName")
                        .getValue(String::class.java) ?: ""

                phone =
                    snapshot.child("phone")
                        .getValue(String::class.java) ?: ""

                location =
                    snapshot.child("location")
                        .getValue(String::class.java) ?: ""

                skill =
                    snapshot.child("skill")
                        .getValue(String::class.java) ?: ""

                bio =
                    snapshot.child("bio")
                        .getValue(String::class.java) ?: ""

                experience =
                    snapshot.child("experience")
                        .getValue(String::class.java) ?: ""

                val imageFromDb =
                    snapshot.child("profileImage")
                        .getValue(String::class.java) ?: ""

                profileImage =
                    if (
                        imageFromDb.isBlank() ||
                        imageFromDb == "null"
                    ) {
                        ""
                    } else {
                        imageFromDb
                    }
            }
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Edit Profile",
                        fontWeight = FontWeight.Bold,
                        color = colorPrimary
                    )
                },

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = colorPrimary
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = lightBg
                )
            )
        }

    ) { paddingValues ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(lightBg)
                .padding(paddingValues)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                contentAlignment = Alignment.BottomEnd
            ) {

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDDE2E9))
                        .clickable {
                            launcher.launch("image/*")
                        },

                    contentAlignment = Alignment.Center
                ) {

                    if (
                        profileImage.isNotBlank() &&
                        profileImage != "null"
                    ) {

                        Image(
                            painter = rememberAsyncImagePainter(profileImage),
                            contentDescription = null,

                            modifier = Modifier.fillMaxSize(),

                            contentScale = ContentScale.Crop
                        )

                    } else {

                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = colorPrimary,
                            modifier = Modifier.size(58.dp)
                        )
                    }
                }

                if (
                    profileImage.isNotBlank() &&
                    profileImage != "null"
                ) {

                    Surface(

                        modifier = Modifier
                            .size(36.dp)
                            .clickable {

                                profileImage = ""
                                imageUri = null
                            },

                        shape = CircleShape,

                        color = Color.Red
                    ) {

                        Box(
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Tap image to change profile photo",
                color = Color.Gray,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Update Your Information",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = colorPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Keep your work profile updated",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = fullName,

                onValueChange = {
                    fullName = it
                },

                label = {
                    Text("Full Name")
                },

                leadingIcon = {

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = colorAccent
                    )
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(18.dp),

                singleLine = true,

                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorAccent,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = phone,

                onValueChange = {
                    phone = it
                },

                label = {
                    Text("Phone Number")
                },

                leadingIcon = {

                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = colorAccent
                    )
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(18.dp),

                singleLine = true,

                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorAccent,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = location,

                onValueChange = {
                    location = it
                },

                label = {
                    Text("Location")
                },

                leadingIcon = {

                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = colorAccent
                    )
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(18.dp),

                singleLine = true,

                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorAccent,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = skill,

                onValueChange = {
                    skill = it
                },

                label = {
                    Text("Skill")
                },

                leadingIcon = {

                    Icon(
                        imageVector = Icons.Default.Handyman,
                        contentDescription = null,
                        tint = colorAccent
                    )
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(18.dp),

                singleLine = true,

                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorAccent,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = experience,

                onValueChange = {
                    experience = it
                },

                label = {
                    Text("Experience")
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(18.dp),

                singleLine = true,

                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorAccent,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = bio,

                onValueChange = {
                    bio = it
                },

                label = {
                    Text("Bio")
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),

                shape = RoundedCornerShape(18.dp),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorAccent,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(

                onClick = {

                    isLoading = true

                    val updates = mapOf(

                        "fullName" to fullName,
                        "phone" to phone,
                        "location" to location,
                        "skill" to skill,
                        "experience" to experience,
                        "bio" to bio,
                        "profileImage" to profileImage
                    )

                    FirebaseDatabase.getInstance().reference
                        .child("users")
                        .child(userId)
                        .updateChildren(updates)
                        .addOnSuccessListener {

                            isLoading = false

                            Toast.makeText(
                                context,
                                "Profile Updated",
                                Toast.LENGTH_SHORT
                            ).show()

                            navController.popBackStack()
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
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )

                } else {

                    Text(
                        text = "Save Changes",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FundiEditProfileScreenPreview() {

    FundiEditProfileScreen(
        rememberNavController()
    )
}