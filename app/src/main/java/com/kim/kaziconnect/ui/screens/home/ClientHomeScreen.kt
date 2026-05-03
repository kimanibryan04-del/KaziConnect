package com.kim.kaziconnect.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
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
import com.kim.kaziconnect.navigation.ROUT_ROLESELECTION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientHomeScreen(navController: NavHostController) {
    // Deep Cobalt Blue and Vibrant Orange branding
    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = true,
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
                    onClick = { navController.navigate(route = ROUT_CLIENTGIG) },
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
                    onClick = {  navController.navigate(route = ROUT_CLIENTMESSAGES) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(lightBg)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // 1. HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Find Help Nearby",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = colorPrimary,
                        letterSpacing = (-0.5).sp
                    )
                    Text("Nairobi, Kenya", fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                }
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    IconButton(onClick = { /* Notifications Logic */ }) {
                        BadgedBox(badge = { Badge(containerColor = colorAccent) }) {
                            Icon(Default.Notifications, contentDescription = null, tint = colorPrimary, modifier = Modifier.size(26.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. SEARCH BAR (Fixed Error Version)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(
                            text = "Search plumbers, painters, masons...",
                            color = colorPrimary.copy(alpha = 0.4f),
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = colorAccent,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    textStyle = TextStyle(
                        color = colorPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        // This is the key: we make the outline transparent
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = colorAccent
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 3. AVAILABLE SERVICES (Grid layout with aspect ratio)
            Text("Available Services", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = colorPrimary)
            Spacer(modifier = Modifier.height(16.dp))

            val services = listOf(
                Triple("Plumbing", Default.Build, Color(0xFFE3F2FD)),
                Triple("Electrical", Default.Bolt, Color(0xFFFFF3E0)),
                Triple("Masonry", Default.Tapas, Color(0xFFE8F5E9)),
                Triple("Painting", Default.Brush, Color(0xFFF3E5F5)),
                Triple("Carpentry", Default.Hardware, Color(0xFFEFEBE9)),
                Triple("Cleaning", Default.LocalLaundryService, Color(0xFFFFEBEE))
            )

            services.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowItems.forEach { (name, icon, bg) ->
                        ServiceCard(name, icon, bg, colorPrimary, Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. FEATURED PROVIDERS (Testing state)
            Text("Featured Providers", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = colorPrimary)
            Box(
                modifier = Modifier.fillMaxWidth().height(140.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = colorAccent, strokeWidth = 3.dp, modifier = Modifier.size(32.dp))
                    Spacer(Modifier.height(12.dp))
                    Text("Searching for experts...", color = Color.Gray.copy(alpha = 0.8f), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 5. ONGOING TASKS (Testing state)
            Text("My Ongoing Tasks", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = colorPrimary)
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(modifier = Modifier.padding(40.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No active tasks found", color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ServiceCard(name: String, icon: ImageVector, iconBg: Color, primaryColor: Color, modifier: Modifier) {
    Surface(
        modifier = modifier.aspectRatio(1f), // Ensures perfect square shape
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(56.dp).background(iconBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = primaryColor, modifier = Modifier.size(26.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text(name, fontWeight = FontWeight.Bold, color = primaryColor, fontSize = 14.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClientHomeScreenPreview() {
    ClientHomeScreen(rememberNavController())
}