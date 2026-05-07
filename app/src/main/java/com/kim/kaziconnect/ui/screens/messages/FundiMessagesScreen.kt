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
import com.kim.kaziconnect.navigation.ROUT_FUNDIHOME
import com.kim.kaziconnect.navigation.ROUT_FUNDIJOB
import com.kim.kaziconnect.navigation.ROUT_FUNDIMESSAGES
import com.kim.kaziconnect.navigation.ROUT_FUNDIPROFILE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundiMessagesScreen(navController: NavHostController) {
    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
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
                    icon = { Icon(Icons.Outlined.Home, null) },
                    label = { Text("Home") },
                    selected = false,
                    onClick = { navController.navigate(ROUT_FUNDIHOME) {
                        launchSingleTop = true
                    } },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.List, null) },
                    label = { Text("My Jobs") },
                    selected = false,
                    onClick = { navController.navigate(ROUT_FUNDIJOB) {
                        launchSingleTop = true
                    } },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Person, null) },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = { navController.navigate(ROUT_FUNDIPROFILE) {
                        launchSingleTop = true
                    } },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Email, null) },
                    label = { Text("Messages") },
                    selected = true,
                    onClick = {navController.navigate(ROUT_FUNDIMESSAGES) {
                        launchSingleTop = true
                    } },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        selectedTextColor = colorAccent,
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
            // Updated text specifically for the Fundi's perspective
            EmptyMessagesView(
                message = "No messages yet",
                description = "Inquiries from clients about your services will appear here.",
                icon = Icons.Outlined.QuestionAnswer
            )
        }
    }
}

@Composable
fun EmptyMessagesView(message: String, description: String, icon: ImageVector) {
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
            color = Color(0xFF1B263B),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = description,
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 50.dp),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FundiMessagesScreenPreview() {
    FundiMessagesScreen(rememberNavController())
}