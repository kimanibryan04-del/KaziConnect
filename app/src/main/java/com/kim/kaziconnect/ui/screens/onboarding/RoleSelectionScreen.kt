package com.kim.kaziconnect.ui.screens.onboarding


import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kim.kaziconnect.data.AuthViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowCompat
import com.kim.kaziconnect.R
import com.kim.kaziconnect.data.AuthViewModelFactory
import com.kim.kaziconnect.navigation.ROUT_CLIENTHOME
import com.kim.kaziconnect.navigation.ROUT_FUNDIHOME
import com.kim.kaziconnect.navigation.ROUT_ROLESELECTION


@Composable
fun RoleSelectionScreen(
    navController: NavHostController,
    fullName: String = ""   // ✅ default value
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().reference
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(navController, context)
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    val colorPrimary = Color(0xFF3D5A80)
    val colorAccent = Color(0xFFEE6C4D)
    val colorTextDark = Color(0xFF2B2D42)

    val vibrantBackground = Brush.radialGradient(
        colors = listOf(Color(0xFFE8F2FF), Color.White),
        center = Offset(Float.POSITIVE_INFINITY, 0f),
        radius = 1800f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(vibrantBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // === RESTORED LOGO SECTION ===
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .background(Color.White, shape = CircleShape)
                    .border(BorderStroke(4.dp, colorAccent), shape = CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.kaziconnect),
                    contentDescription = "KaziConnect Logo",
                    modifier = Modifier.size(350.dp),
                    contentScale = ContentScale.Fit
                )
            }
            // =============================

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Welcome to KaziConnect",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = colorTextDark,
                fontFamily = FontFamily.SansSerif
            )

            Text(
                text = "Trusted Blue-Collar Services in Nairobi",
                fontSize = 18.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 10.dp),
                fontFamily = FontFamily.SansSerif
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "I am using this app to:",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif
            )

            Spacer(modifier = Modifier.height(30.dp))

            TextButton(
                onClick = {
                    val userId = auth.currentUser?.uid

                    val userMap = mapOf(
                        "uid" to userId,
                        "name" to fullName,   // ✅ REAL NAME from RegisterScreen
                        "email" to auth.currentUser?.email,
                        "role" to "client"
                    )

                    if (userId != null) {
                        database.child("users").child(userId).setValue(userMap)
                            .addOnSuccessListener {
                                navController.navigate(ROUT_CLIENTHOME)// ✅ navigate AFTER saving
                            }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = colorPrimary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Text(
                    text = "HIRE SERVICE PROVIDERS",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                onClick = {
                    val userId = auth.currentUser?.uid

                    val userMap = mapOf(
                        "uid" to userId,
                        "name" to fullName,   // ✅ REAL NAME from RegisterScreen
                        "email" to auth.currentUser?.email,
                        "role" to "fundi"
                    )

                    if (userId != null) {
                        database.child("users").child(userId).setValue(userMap)
                            .addOnSuccessListener {
                                navController.navigate(ROUT_FUNDIHOME)   // ✅ navigate AFTER saving
                            }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(2.dp, colorAccent)
            ) {
                Text(
                    text = "FIND JOB OPPORTUNITIES",
                    color = colorAccent,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
@Preview(showBackground = true)
@Composable
fun RoleSelectionScreenPreview() {
    RoleSelectionScreen(rememberNavController())
}