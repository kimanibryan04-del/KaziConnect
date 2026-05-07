package com.kim.kaziconnect.ui.screens.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kim.kaziconnect.navigation.ROUT_CLIENTGIG
import com.kim.kaziconnect.navigation.ROUT_CLIENTHOME
import com.kim.kaziconnect.navigation.ROUT_CLIENTMESSAGES
import com.kim.kaziconnect.navigation.ROUT_CLIENTPROFILE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientMessagesScreen(navController: NavHostController) {
    // Branding Colors matching Kaziconnect Identity
    val colorPrimary = Color(0xFF1B263B) // Cobalt Blue
    val colorAccent = Color(0xFFEE6C4D)  // Vibrant Orange
    val lightBg = Color(0xFFF1F4F9)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Messages", fontWeight = FontWeight.Black, color = colorPrimary)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = {  navController.navigate(ROUT_CLIENTHOME) {
                        launchSingleTop = true
                    } },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        selectedTextColor = colorAccent,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = "Gigs") },
                    label = { Text("Gigs") },
                    selected = false,
                    onClick = { navController.navigate(ROUT_CLIENTGIG) {
                        launchSingleTop = true
                    } },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        selectedTextColor = colorAccent,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = {navController.navigate(ROUT_CLIENTPROFILE) {
                        launchSingleTop = true
                    } },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        selectedTextColor = colorAccent,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Email, contentDescription = "Messages") },
                    label = { Text("Messages") },
                    selected = true,
                    onClick = { navController.navigate(ROUT_CLIENTMESSAGES) {
                        launchSingleTop = true
                    } },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        selectedTextColor = colorAccent,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(lightBg)
        ) {
            // Empty state for testing - No data yet
            EmptyMessagesView(
                message = "No conversations yet",
                icon = Icons.Outlined.QuestionAnswer
            )
        }
    }
}

@Composable
fun EmptyMessagesView(message: String, icon: ImageVector) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.LightGray.copy(alpha = 0.6f)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = message,
            color = Color.Gray,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Your chats with service providers will appear here.",
            color = Color.LightGray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 40.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ClientMessagesScreenPreview() {
    ClientMessagesScreen(rememberNavController())
}