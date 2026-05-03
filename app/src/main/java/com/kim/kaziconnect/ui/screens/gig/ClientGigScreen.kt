package com.kim.kaziconnect.ui.screens.gig

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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
fun  ClientGigScreen(navController: NavHostController) {
    // Branding Colors
    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Active", "Pending", "Completed")

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(Color.White)) {
                CenterAlignedTopAppBar(
                    title = {
                        Text("My Gigs", fontWeight = FontWeight.Black, color = colorPrimary)
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
                SecondaryTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = colorAccent
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedTab == index) colorAccent else Color.Gray
                                )
                            }
                        )
                    }
                }
            }
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
                    onClick = {  navController.navigate(route = ROUT_CLIENTHOME) },
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
                    selected = true,
                    onClick = {  navController.navigate(route = ROUT_CLIENTGIG) },
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
                    onClick = { navController.navigate(route = ROUT_CLIENTPROFILE) },
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
                    selected = false,
                    onClick = { navController.navigate(route = ROUT_CLIENTMESSAGES) },
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
            // Content matches your request to stay blank for testing
            when (selectedTab) {
                0 -> EmptyStateView("You have no active gigs", Icons.Default.Search)
                1 -> EmptyStateView("No pending requests", Icons.Default.Info)
                2 -> EmptyStateView("No past history", Icons.Default.History)
            }
        }
    }
}

@Composable
fun EmptyStateView(message: String, icon: ImageVector) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color.LightGray
        )
        Spacer(Modifier.height(16.dp))
        // Fixed: The message is now correctly passed into the Text composable
        Text(
            text = message,
            color = Color.Gray,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ClientGigScreenPreview() {
    ClientGigScreen(rememberNavController())
}