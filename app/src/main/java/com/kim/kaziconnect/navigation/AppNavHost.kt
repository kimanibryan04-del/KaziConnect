package com.kim.kaziconnect.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kim.kaziconnect.ui.screens.roleselection.RoleSelectionScreen
import com.kim.kaziconnect.ui.screens.splash.SplashScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_REGISTER
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ROUT_SPLASH) {
            SplashScreen(navController)
        }
        composable(ROUT_ROLESELECTION) {
            RoleSelectionScreen(navController)
        }
        composable(ROUT_REGISTER) {
            RoleSelectionScreen(navController)
        }
        composable(ROUT_LOGIN) {
            RoleSelectionScreen(navController)
        }


    }

}

