package com.kim.kaziconnect.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.kim.kaziconnect.navigation.ROUT_FUNDIHOME
import com.kim.kaziconnect.navigation.ROUT_FUNDIJOB
import com.kim.kaziconnect.navigation.ROUT_FUNDIMESSAGES
import com.kim.kaziconnect.navigation.ROUT_FUNDIPROFILE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundiProfileScreen(navController: NavHostController) {
    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    Scaffold(
        modifier = Modifier.statusBarsPadding(), // Fixed: Pushes header below system status bar
        topBar = {
            Surface(shadowElevation = 0.dp) {
                Text(
                    text = "Profile",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(vertical = 20.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = colorPrimary
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Home, null) },
                    label = { Text("Home") },
                    selected = false,
                    onClick = { navController.navigate(ROUT_FUNDIHOME) {
                        launchSingleTop = true
                    } }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.List, null) },
                    label = { Text("My Jobs") },
                    selected = false,
                    onClick = { navController.navigate(ROUT_FUNDIJOB) {
                        launchSingleTop = true
                    } }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, null) },
                    label = { Text("Profile") },
                    selected = true,
                    onClick = {navController.navigate(ROUT_FUNDIPROFILE) {
                        launchSingleTop = true
                    } },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        selectedTextColor = colorAccent,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Email, null) },
                    label = { Text("Messages") },
                    selected = false,
                    onClick = { navController.navigate(ROUT_FUNDIMESSAGES) {
                        launchSingleTop = true
                    } }
                )
            }
        }
    ) { paddingValues ->
        // ... (rest of your Column and helper functions remain exactly the same)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(lightBg)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.padding(20.dp),
                    tint = Color.LightGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Pro Fundi", fontSize = 24.sp, fontWeight = FontWeight.Black, color = colorPrimary)
            Text("General Handyman • Nairobi", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProfileMetricCard("Rating", "5.0", Icons.Default.Star, Color(0xFFFFD700), Modifier.weight(1f))
                ProfileMetricCard("Jobs Done", "24", Icons.Default.Handyman, colorAccent, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {

                // 1. ACCOUNT SETTINGS
                ProfileSectionHeader("Account Settings")
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(0.5.dp)
                ) {
                    Column {
                        ProfileMenuOption("Personal Information", Icons.Outlined.Badge, colorPrimary)
                        ProfileMenuOption("Payment Details", Icons.Outlined.Wallet, colorPrimary, isLast = true)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. WORK SETTINGS
                ProfileSectionHeader("Work Settings")
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(0.5.dp)
                ) {
                    Column {
                        ProfileMenuOption("Edit Skills", Icons.Outlined.Build, colorPrimary)
                        ProfileMenuOption("Work Preferences", Icons.Outlined.Settings, colorPrimary, isLast = true)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3. PREFERENCES (Matches Client Screen)
                ProfileSectionHeader("Preferences")
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(0.5.dp)
                ) {
                    Column {
                        ProfileMenuOption("Notifications", Icons.Outlined.Notifications, colorPrimary)
                        ProfileMenuOption("App Theme", Icons.Outlined.DarkMode, colorPrimary, isLast = true)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 4. SUPPORT (Matches Client Screen)
                ProfileSectionHeader("Support")
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(0.5.dp)
                ) {
                    ProfileMenuOption("Help Center", Icons.Outlined.HelpOutline, colorPrimary, isLast = true)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 5. LOGOUT
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(0.5.dp)
                ) {
                    ProfileMenuOption("Logout", Icons.Outlined.Logout, Color.Red, isLast = true)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
@Composable
fun ProfileMetricCard(label: String, value: String, icon: ImageVector, iconColor: Color, modifier: Modifier) {
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
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color(0xFF1B263B))
            Text(label, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ProfileSectionHeader(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )
}

@Composable
fun ProfileMenuOption(title: String, icon: ImageVector, color: Color, isLast: Boolean = false) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = color.copy(alpha = 0.7f), modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = color,
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
        }
        if (!isLast) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = Color(0xFFF1F4F9)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FundiProfileScreenPreview() {
    FundiProfileScreen(rememberNavController())
}