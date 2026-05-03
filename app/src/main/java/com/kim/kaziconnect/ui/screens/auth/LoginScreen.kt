package com.kim.kaziconnect.ui.screens.auth.login

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kim.kaziconnect.R
import com.kim.kaziconnect.navigation.ROUT_CLIENTHOME
import com.kim.kaziconnect.navigation.ROUT_ROLESELECTION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    val view = LocalView.current
    val focusManager = LocalFocusManager.current

    // Set Status Bar icons to dark for the light background
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    // Concept 2 Color Palette
    val colorPrimary = Color(0xFF3D5A80) // Cobalt Blue
    val colorAccent = Color(0xFFEE6C4D)  // Vibrant Orange
    val topGradientColor = Color(0xFFF0F7FF) // Soft Blue tint

    // State Management
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Validation Logic
    val isLoginEnabled = email.isNotBlank() && password.isNotBlank() && !isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(topGradientColor, Color.White)
                )
            )
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.10f))

        // 1. BRANDING: Circular Logo with Orange Border
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(3.dp, colorAccent, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.kaziconnect),
                contentDescription = "KaziConnect Logo",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. TEXT HEADERS
        Text(
            text = "Welcome Back",
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = colorPrimary,
            fontFamily = FontFamily.SansSerif
        )
        Text(
            text = "Login to continue to KaziConnect",
            fontSize = 15.sp,
            color = Color.Gray.copy(alpha = 0.8f),
            fontFamily = FontFamily.SansSerif
        )

        Spacer(modifier = Modifier.height(30.dp))

        // 3. ERROR FEEDBACK
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp),
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif
            )
        }

        // 4. INPUT FIELDS
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                errorMessage = null
            },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            isError = errorMessage != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorAccent,
                focusedLabelColor = colorAccent
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        // PASSWORD FIELD - Simplified to remove 'Icons' dependency error
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            isError = errorMessage != null,
            singleLine = true,
            // Keep the masking logic even without the icon
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            trailingIcon = {
                // We use a simple Text button instead of the 'Icons' library
                TextButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(
                        text = if (passwordVisible) "HIDE" else "SHOW",
                        color = colorAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorAccent,
                focusedLabelColor = colorAccent
            )
        )

        // Forgot Password (UI-only placeholder)
        TextButton(
            onClick = { /* Non-functional for MVP */ },
            modifier = Modifier.align(Alignment.End),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Forgot Password?",
                color = colorPrimary.copy(alpha = 0.7f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 5. ACTION BUTTON WITH LOADING STATE
        Button(
            onClick = {
                isLoading = true
                errorMessage = null
                // Trigger your Django login logic here
                navController.navigate(route = ROUT_ROLESELECTION)
            },
            enabled = isLoginEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorPrimary,
                disabledContainerColor = colorPrimary.copy(alpha = 0.5f)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    "LOG IN",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }

        // 6. REGISTRATION LINK
        Row(
            modifier = Modifier.padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "New to KaziConnect? ",
                color = Color.Gray,
                fontSize = 15.sp,
                fontFamily = FontFamily.SansSerif
            )
            TextButton(
                onClick = { navController.navigate("register") },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.height(IntrinsicSize.Min)
            ) {
                Text(
                    text = "Sign Up",
                    color = colorAccent,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Empowering Skilled Labor in Nairobi",
            fontSize = 12.sp,
            color = Color.Gray.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 24.dp),
            fontFamily = FontFamily.SansSerif
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}