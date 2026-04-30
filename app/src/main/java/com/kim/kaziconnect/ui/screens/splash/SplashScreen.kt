package com.kim.kaziconnect.ui.screens.splash

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kim.kaziconnect.R
import com.kim.kaziconnect.navigation.ROUT_REGISTER
import com.kim.kaziconnect.navigation.ROUT_ROLESELECTION
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SplashScreen(navController: NavHostController) {

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // This makes the status bar icons dark (Gray/Black)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    val coroutinescope = rememberCoroutineScope()
    coroutinescope.launch {

        delay(timeMillis = 2000)
        navController.navigate(route = ROUT_REGISTER)
    }

    Column(
        modifier = Modifier.background(color = Color.White)
            .fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.kaziconnect),
            contentDescription = "product",
            modifier = Modifier.size(300.dp)
        )


    }
}
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    SplashScreen(rememberNavController())
}