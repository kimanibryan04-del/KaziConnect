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
                    onClick = { navController.navigate(route = ROUT_CLIENTHOME) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = "Gigs") },
                    label = { Text("Gigs") },
                    selected = false,
                    onClick = { navController.navigate(route = ROUT_CLIENTGIG) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = true,
                    onClick = { /* Stay on current screen */ },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        selectedTextColor = colorAccent,
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
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    ) { paddingValues ->
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
                    color = Color(0xFFDDE2E9)
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

                // Stats Row: Rating and Jobs Requested (Opposite of Fundi "Jobs Done")
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProfileMetricCard("Rating", "4.9", Icons.Default.Star, Color(0xFFFFD700), Modifier.weight(1f))
                    ProfileMetricCard("Jobs Requested", "8", Icons.Default.History, colorAccent, Modifier.weight(1f))
                }

                Spacer(Modifier.height(24.dp))
            }

            item {
                // Account Settings (Saved Locations Removed)
                ProfileGroup(title = "Account Settings") {
                    ProfileItem("Personal Information", Icons.Outlined.Badge)
                    ProfileItem("Payment Methods", Icons.Outlined.Payments)
                }

                ProfileGroup(title = "Preferences") {
                    ProfileItem("Notifications", Icons.Outlined.Notifications)
                    ProfileItem("App Theme", Icons.Outlined.DarkMode)
                }

                ProfileGroup(title = "Support") {
                    ProfileItem("Help Center", Icons.Outlined.HelpOutline)
                }

                // Logout Item as a single card
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    ProfileItem("Logout", Icons.Outlined.Logout, iconColor = Color.Red)
                }

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

// --- HELPER COMPONENTS (DEFINED OUTSIDE MAIN FUNCTION) ---


// Define this outside your function
data class MetricData(
    val label: String,
    val value: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun ProfileMetricCard(
    data: MetricData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.5.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(data.icon, null, tint = data.color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(data.value, fontWeight = FontWeight.Black, fontSize = 18.sp)
            Text(data.label, fontSize = 12.sp, color = Color.Gray)
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
fun ProfileItem(text: String, icon: ImageVector, iconColor: Color = Color(0xFF1B263B)) {
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
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(16.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                color = if (iconColor == Color.Red) Color.Red else Color(0xFF1B263B)
            )
            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClientProfileScreenPreview() {
    ClientProfileScreen(rememberNavController())
}