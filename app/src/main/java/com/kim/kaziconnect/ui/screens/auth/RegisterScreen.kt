package com.kim.kaziconnect.ui.screens.register

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
fun RegisterScreen(navController: NavHostController) {
    val view = LocalView.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

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
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Validation Logic: SIGN UP only active if all fields valid and passwords match
    val passwordsMatch = password == confirmPassword && password.isNotEmpty()
    val isRegisterEnabled = fullName.isNotBlank() &&
            email.isNotBlank() &&
            passwordsMatch &&
            !isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(topGradientColor, Color.White)))
            .padding(horizontal = 30.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.08f).height(60.dp))

        // 1. BRANDING
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(3.dp, colorAccent, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.kaziconnect),
                contentDescription = "KaziConnect Logo",
                modifier = Modifier.fillMaxSize().padding(12.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 2. TEXT HEADERS
        Text(
            text = "Create Account",
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = colorPrimary,
            fontFamily = FontFamily.SansSerif
        )
        Text(
            text = "Join KaziConnect today",
            fontSize = 15.sp,
            color = Color.Gray.copy(alpha = 0.8f),
            fontFamily = FontFamily.SansSerif
        )

        Spacer(modifier = Modifier.height(30.dp))

        // 3. INPUT FIELDS
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colorAccent, focusedLabelColor = colorAccent)
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colorAccent, focusedLabelColor = colorAccent)
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            trailingIcon = {
                TextButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(if (passwordVisible) "HIDE" else "SHOW", color = colorAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colorAccent, focusedLabelColor = colorAccent)
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Confirm Password Field
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            // UI Feedback: Red border if passwords mismatch
            isError = confirmPassword.isNotEmpty() && !passwordsMatch,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (passwordsMatch) colorPrimary else colorAccent,
                focusedLabelColor = colorAccent
            )
        )

        // Password Match Feedback Label
        if (confirmPassword.isNotEmpty() && !passwordsMatch) {
            Text(
                text = "Passwords do not match",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Start).padding(start = 8.dp, top = 4.dp),
                fontFamily = FontFamily.SansSerif
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 4. ACTION BUTTON (With Loading state)
        Button(
            onClick = { isLoading = true
                navController.navigate(route = ROUT_ROLESELECTION)
                      },
            enabled = isRegisterEnabled,
            modifier = Modifier.fillMaxWidth().height(58.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorPrimary,
                disabledContainerColor = colorPrimary.copy(alpha = 0.5f)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
            } else {
                Text("SIGN UP", fontWeight = FontWeight.Bold, fontSize = 16.sp, letterSpacing = 1.sp)
            }
        }

        // 5. LOGIN LINK
        Row(
            modifier = Modifier.padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Already have an account? ", color = Color.Gray, fontSize = 15.sp)
            TextButton(
                onClick = { navController.navigate("login") },
                contentPadding = PaddingValues(0.dp),
            ) {
                Text("Login", color = colorAccent, fontWeight = FontWeight.Black, fontSize = 15.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Trusted Blue-Collar Services in Nairobi",
            fontSize = 11.sp,
            color = Color.Gray.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 20.dp),
            fontFamily = FontFamily.SansSerif
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(rememberNavController())
}