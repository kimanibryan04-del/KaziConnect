package com.kim.kaziconnect.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun ClientProfileScreen(navController: NavHostController) {
    val colorPrimary = Color(0xFF1B263B) // Cobalt Blue
    val colorAccent = Color(0xFFEE6C4D)  // Vibrant Orange
    val lightBg = Color(0xFFF1F4F9)     // Light Gray/Blue background

    Scaffold(
        topBar = {
            // Top Section is White as seen in image_eec8e8.png
            CenterAlignedTopAppBar(
                title = {
                    Text("Profile", fontWeight = FontWeight.Black, color = colorPrimary)
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
                    selected = false,
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
                    selected = true,
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
        // The Body uses the lightBg color to match image_eec8e8.png
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(lightBg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(Modifier.height(30.dp))
                // Profile Avatar Section
                Surface(
                    modifier = Modifier.size(120.dp),
                    shape = CircleShape,
                    color = Color(0xFFDDE2E9) // Matching the circle in image_eec8e8.png
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.padding(25.dp),
                        tint = colorPrimary
                    )
                }
                Spacer(Modifier.height(16.dp))
                Text("Bryan", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = colorPrimary)
                Text("Nairobi, Kenya", color = Color.Gray, fontSize = 14.sp)
                Spacer(Modifier.height(32.dp))
            }

            // Menu Groups
            item {
                ProfileGroup(title = "Account Settings") {
                    ProfileItem("Personal Information", Icons.Outlined.Badge)
                    ProfileItem("Saved Locations", Icons.Outlined.LocationOn)
                    ProfileItem("Payment Methods", Icons.Outlined.Payments)
                }
                ProfileGroup(title = "Preferences") {
                    ProfileItem("Notifications", Icons.Outlined.Notifications)
                    ProfileItem("App Theme", Icons.Outlined.DarkMode)
                }
                ProfileGroup(title = "Support") {
                    ProfileItem("Help Center", Icons.Outlined.HelpOutline)
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun ProfileGroup(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        content()
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun ProfileItem(text: String, icon: ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = Color(0xFF1B263B), modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(16.dp))
            Text(text, fontSize = 16.sp, modifier = Modifier.weight(1f), color = Color(0xFF1B263B))
            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClientProfileScreenPreview() {
    ClientProfileScreen(rememberNavController())
}