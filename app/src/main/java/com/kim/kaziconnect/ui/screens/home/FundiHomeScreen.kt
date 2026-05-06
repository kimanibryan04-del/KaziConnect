package com.kim.kaziconnect.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
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
import com.kim.kaziconnect.navigation.ROUT_FUNDIHOME
import com.kim.kaziconnect.navigation.ROUT_FUNDIJOB
import com.kim.kaziconnect.navigation.ROUT_FUNDIMESSAGES
import com.kim.kaziconnect.navigation.ROUT_FUNDIPROFILE
import com.kim.kaziconnect.navigation.ROUT_REGISTER

// Data Model to be moved to a separate file tomorrow
data class JobModel(
    val id: String = "",
    val title: String = "",
    val location: String = "",
    val budget: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundiHomeScreen(navController: NavHostController) {
    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    val ongoingTasks = listOf<JobModel>()
    val availableJobs = listOf<JobModel>()

    Scaffold(
        modifier = Modifier.statusBarsPadding(), // Ensures the back button isn't hidden by the status bar
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, "Home") },
                    label = { Text("Home") },
                    selected = true,
                    onClick = { navController.navigate(route = ROUT_FUNDIHOME) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        selectedTextColor = colorAccent,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.List, "My Jobs") },
                    label = { Text("My Jobs") },
                    selected = false,
                    onClick = { navController.navigate(route = ROUT_FUNDIJOB) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Person, "Profile") },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = { navController.navigate(route = ROUT_FUNDIPROFILE) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Email, "Messages") },
                    label = { Text("Messages") },
                    selected = false,
                    onClick = { navController.navigate(route = ROUT_FUNDIMESSAGES) }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(lightBg)
                .padding(horizontal = 20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))

                // 1. HEADER WITH PREVIOUS (BACK) BUTTON
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Previous Button
                    IconButton(
                        onClick = {navController.navigate(route = ROUT_REGISTER) }, // Replace "register" with your actual ROUT_REGISTER constant
                        modifier = Modifier.background(Color.White, CircleShape).size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Register",
                            tint = colorPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Work Dashboard",
                            fontSize = 24.sp, // Adjusted slightly to fit with the back button
                            fontWeight = FontWeight.Black,
                            color = colorPrimary,
                            letterSpacing = (-0.5).sp
                        )
                        Text("Nairobi, Kenya", fontSize = 14.sp, color = Color.Gray)
                    }

                    Surface(shape = CircleShape, color = Color.White, shadowElevation = 2.dp) {
                        IconButton(onClick = { }) {
                            BadgedBox(badge = { Badge(containerColor = colorAccent) }) {
                                Icon(Default.Notifications, null, tint = colorPrimary)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ... rest of your code (Search, Stats, etc.) remains the same
                // 2. SEARCH
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Search for jobs in Roysambu...", fontSize = 14.sp) },
                        leadingIcon = { Icon(Default.Search, null, tint = colorAccent) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 3. STATS
                Text("Your Performance", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = colorPrimary)
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatCard("Earnings", "KES 0", Default.Payments, colorAccent, Modifier.weight(1f))
                    StatCard("Rating", "5.0", Default.Star, Color(0xFFFFD700), Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            // Ongoing Tasks and Available Jobs sections follow here...
            // 4. ONGOING TASKS SECTION
            item {
                Text("Ongoing Tasks", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = colorPrimary)
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (ongoingTasks.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
                    ) {
                        Box(modifier = Modifier.padding(30.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text("No active tasks", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            } else {
                items(ongoingTasks) { task ->
                    CleanJobCard(task, colorPrimary, colorAccent)
                }
            }

            // 5. AVAILABLE GIGS SECTION
            item {
                Text("Available Jobs Near You", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = colorPrimary)
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (availableJobs.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = colorAccent, strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
                        Spacer(Modifier.height(12.dp))
                        Text("Looking for new opportunities...", color = Color.Gray, fontSize = 13.sp)
                    }
                }
            } else {
                items(availableJobs) { job ->
                    CleanJobCard(job, colorPrimary, colorAccent)
                }
            }

            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
    }
}

@Composable
fun CleanJobCard(job: JobModel, colorPrimary: Color, colorAccent: Color) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(job.title, fontWeight = FontWeight.Bold, color = colorPrimary, fontSize = 16.sp)
                Text(job.location, color = Color.Gray, fontSize = 13.sp)
            }
            Text(job.budget, fontWeight = FontWeight.Black, color = colorAccent, fontSize = 16.sp)
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, iconColor: Color, modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(8.dp))
            Text(title, color = Color.Gray, fontSize = 12.sp)
            Text(value, color = Color(0xFF1B263B), fontSize = 18.sp, fontWeight = FontWeight.Black)
        }
    }
}






@Preview(showBackground = true)
@Composable
fun FundiHomeScreenPreview() {
    FundiHomeScreen(rememberNavController())
}