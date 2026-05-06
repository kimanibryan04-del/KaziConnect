package com.kim.kaziconnect.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kim.kaziconnect.ui.screens.gig.ClientGigScreen
import com.kim.kaziconnect.ui.screens.gig.FundiJobScreen
import com.kim.kaziconnect.ui.screens.home.ClientHomeScreen
import com.kim.kaziconnect.ui.screens.home.FundiHomeScreen
import com.kim.kaziconnect.ui.screens.login.LoginScreen
import com.kim.kaziconnect.ui.screens.messages.ClientMessagesScreen
import com.kim.kaziconnect.ui.screens.messages.FundiMessagesScreen
import com.kim.kaziconnect.ui.screens.profile.ClientProfileScreen
import com.kim.kaziconnect.ui.screens.register.RegisterScreen
import com.kim.kaziconnect.ui.screens.onboarding.RoleSelectionScreen
import com.kim.kaziconnect.ui.screens.profile.FundiProfileScreen
import com.kim.kaziconnect.ui.screens.splash.SplashScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_SPLASH
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ROUT_SPLASH) {
            SplashScreen(navController)
        }
        composable(
            route = "$ROUT_ROLESELECTION/{fullName}"
        ) { backStackEntry ->

            val fullName = backStackEntry.arguments?.getString("fullName") ?: ""

            RoleSelectionScreen(navController, fullName)
        }
        composable(ROUT_REGISTER) {
            RegisterScreen(navController)
        }
        composable(ROUT_LOGIN) {
            LoginScreen(navController)
        }
        composable(ROUT_CLIENTHOME) {
            ClientHomeScreen(navController)
        }
        composable(ROUT_CLIENTGIG) {
            ClientGigScreen(navController)
        }
        composable(ROUT_CLIENTMESSAGES) {
            ClientMessagesScreen(navController)
        }
        composable(ROUT_CLIENTPROFILE) {
            ClientProfileScreen(navController)
        }
        composable(ROUT_FUNDIHOME) {
            FundiHomeScreen(navController)
        }
        composable(ROUT_FUNDIJOB) {
            FundiJobScreen(navController)
        }
        composable(ROUT_FUNDIPROFILE) {
            FundiProfileScreen(navController)
        }
        composable(ROUT_FUNDIMESSAGES) {
            FundiMessagesScreen(navController)
        }


    }

}

