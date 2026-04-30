package com.kim.kaziconnect.ui.screens.roleselection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kim.kaziconnect.R
import android.app.Activity
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat



@Composable
fun RoleSelectionScreen(navController: NavHostController) {

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // This makes the status bar icons dark (Gray/Black)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }
    // Colors from your scheme
    val colorPrimary = Color(0xFF3D5A80) // Cobalt Blue (Client)
    val colorAccent = Color(0xFFEE6C4D)  // Orange-Yellow (Fundi)
    val colorTextDark = Color(0xFF2B2D42)

    // Vibrant Radial Gradient background
    val vibrantBackground = Brush.radialGradient(
        colors = listOf(
            Color(0xFFE8F2FF), // Very strong, vibrant core
            Color.White        // Softening to pure white at the edges
        ),
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
            Spacer(modifier = Modifier.height(50.dp)) // Safe area/Top spacing

            // ==========================================================
            // LOGO IN A CIRCULAR BORDER (Improved UI)
            // ==========================================================
            Box(
                modifier = Modifier
                    .size(180.dp) // Total size of the circular container
                    // 1. Give it a subtle background color to make the white part of the shield pop
                    .background(Color.White, shape = CircleShape)
                    // 2. Define the exact, crisp circular border matching your Orange-Yellow accent
                    .border(BorderStroke(4.dp, colorAccent), shape = CircleShape)
                    // 3. Ensure everything inside is clipped to the circle (no leaking corners)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center // Center the logo image perfectly inside the box
            ) {
                Image(
                    painter = painterResource(id = R.drawable.kaziconnect), // Removed .png from ID call
                    contentDescription = "KaziConnect Logo",
                    // Increase size slightly so the shield fills the container nicely
                    modifier = Modifier.size(350.dp),
                    contentScale = ContentScale.Fit // Ensure the whole shield is visible
                )
            }
            // ==========================================================

            Spacer(modifier = Modifier.height(48.dp)) // Space after the new logo

            // Main Greeting
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

            Spacer(modifier = Modifier.weight(1f)) // Push buttons to comfortable thumb zone

            // ... (Rest of the code for buttons remains the same as before, already polished)

            Text(
                text = "I am using this app to:",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif
            )

            Spacer(modifier = Modifier.height(30.dp))

            // CLIENT BUTTON (Primary Cobalt Blue)
            TextButton(
                onClick = { navController.navigate("register") },
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

            // FUNDI BUTTON (Outlined Orange Accent)
            OutlinedButton(
                onClick = { navController.navigate("register") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = MaterialTheme.shapes.medium,
                // This line was fixed in the previous conversation
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

            Spacer(modifier = Modifier.height(40.dp)) // Bottom padding
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RoleSelectionScreenPreview() {
    RoleSelectionScreen(rememberNavController())
}