package com.kim.kaziconnect.ui.screens.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kim.kaziconnect.models.NotificationData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundiNotificationScreen(navController: NavHostController) {

    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().reference
    val userId = auth.currentUser?.uid ?: ""

    val notifications = remember { mutableStateListOf<NotificationData>() }

    // LOAD NOTIFICATIONS + MARK AS READ
    LaunchedEffect(Unit) {

        // LISTEN FOR LIVE NOTIFICATIONS
        database.child("notifications").child(userId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    notifications.clear()

                    for (notificationSnapshot in snapshot.children) {

                        try {

                            val notification =
                                notificationSnapshot.getValue(NotificationData::class.java)

                            if (notification != null) {
                                notifications.add(notification)
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        // MARK NOTIFICATIONS AS READ
        database.child("notifications").child(userId)
            .get()
            .addOnSuccessListener { snapshot ->

                for (notificationSnapshot in snapshot.children) {

                    notificationSnapshot.ref
                        .child("read")
                        .setValue(true)
                }
            }
    }

    // COLORS
    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notifications",
                        fontWeight = FontWeight.Bold,
                        color = colorPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = colorPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = lightBg
                )
            )
        }
    ) { paddingValues ->

        if (notifications.isEmpty()) {

            // EMPTY STATE
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(lightBg)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 4.dp
                    ) {
                        Box(
                            modifier = Modifier.size(90.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = colorAccent,
                                modifier = Modifier.size(42.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "No Notifications Yet",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorPrimary,
                        fontFamily = FontFamily.SansSerif
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Updates and alerts will appear here",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

        } else {

            // NOTIFICATION LIST
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(lightBg)
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }

                items(notifications.reversed()) { notification ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalAlignment = Alignment.Top
                        ) {

                            // ICON
                            Surface(
                                shape = CircleShape,
                                color = colorAccent.copy(alpha = 0.12f)
                            ) {
                                Box(
                                    modifier = Modifier.size(52.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Work,
                                        contentDescription = null,
                                        tint = colorAccent,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = notification.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = colorPrimary
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = notification.message,
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = notification.time,
                                    color = colorAccent,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FundiNotificationScreenPreview() {
    FundiNotificationScreen(rememberNavController())
}