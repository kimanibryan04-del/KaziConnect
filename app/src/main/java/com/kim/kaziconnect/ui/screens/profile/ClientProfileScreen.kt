package com.kim.kaziconnect.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kim.kaziconnect.navigation.ROUT_CLIENTEDITPROFILE
import com.kim.kaziconnect.navigation.ROUT_CLIENTGIG
import com.kim.kaziconnect.navigation.ROUT_CLIENTHOME
import com.kim.kaziconnect.navigation.ROUT_CLIENTMESSAGES
import com.kim.kaziconnect.navigation.ROUT_CLIENTNOTIFICATION
import com.kim.kaziconnect.navigation.ROUT_CLIENTPROFILE
import com.kim.kaziconnect.navigation.ROUT_PAYMENTMETHOD
import com.kim.kaziconnect.navigation.ROUT_REGISTER

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientProfileScreen(navController: NavHostController) {

    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    val userId =
        FirebaseAuth.getInstance().currentUser?.uid ?: ""

    var name by remember {
        mutableStateOf("")
    }

    var location by remember {
        mutableStateOf("Nairobi, Kenya")
    }

    var rating by remember {
        mutableDoubleStateOf(0.0)
    }

    var profileImage by remember {
        mutableStateOf("")
    }

    var jobsRequested by remember {
        mutableIntStateOf(0)
    }

    /*
    MESSAGE DOT
     */
    var hasUnreadMessages by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {

        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(userId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    name = snapshot.child("name")
                        .getValue(String::class.java) ?: ""

                    location =
                        snapshot.child("location")
                            .getValue(String::class.java)
                            ?: ""

                    rating =
                        snapshot.child("rating")
                            .getValue(Double::class.java) ?: 0.0

                    val imageFromDb =
                        snapshot.child("profileImage")
                            .getValue(String::class.java) ?: ""

                    profileImage =
                        if (
                            imageFromDb.isBlank() ||
                            imageFromDb == "null"
                        ) {
                            ""
                        } else {
                            imageFromDb
                        }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        FirebaseDatabase.getInstance().reference
            .child("jobs")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    var count = 0

                    for (jobSnapshot in snapshot.children) {

                        val clientId =
                            jobSnapshot.child("clientId")
                                .getValue(String::class.java) ?: ""

                        if (clientId == userId) {
                            count++
                        }
                    }

                    jobsRequested = count
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        /*
        LISTEN FOR UNREAD MESSAGES
         */
        FirebaseDatabase.getInstance().reference
            .child("chats")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    hasUnreadMessages = false

                    for (chatSnapshot in snapshot.children) {

                        val participants =
                            chatSnapshot.child("participants")

                        if (participants.hasChild(userId)) {

                            val unread =
                                chatSnapshot.child("unreadCount")
                                    .child(userId)
                                    .getValue(Int::class.java) ?: 0

                            if (unread > 0) {

                                hasUnreadMessages = true
                                break
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    Scaffold(

        topBar = {

            CenterAlignedTopAppBar(

                title = {

                    Text(
                        "Profile",
                        fontWeight = FontWeight.Black,
                        color = colorPrimary
                    )
                },

                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },

        bottomBar = {

            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {

                NavigationBarItem(

                    icon = {
                        Icon(
                            Icons.Outlined.Home,
                            contentDescription = "Home"
                        )
                    },

                    label = {
                        Text("Home")
                    },

                    selected = false,

                    onClick = {

                        navController.navigate(ROUT_CLIENTHOME) {
                            launchSingleTop = true
                        }
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(

                    icon = {
                        Icon(
                            Icons.Filled.List,
                            contentDescription = "Gigs"
                        )
                    },

                    label = {
                        Text("Gigs")
                    },

                    selected = false,

                    onClick = {

                        navController.navigate(ROUT_CLIENTGIG) {
                            launchSingleTop = true
                        }
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(

                    icon = {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = "Profile"
                        )
                    },

                    label = {
                        Text("Profile")
                    },

                    selected = true,

                    onClick = {

                        navController.navigate(ROUT_CLIENTPROFILE) {
                            launchSingleTop = true
                        }
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        selectedTextColor = colorAccent,
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(

                    icon = {

                        BadgedBox(

                            badge = {

                                if (hasUnreadMessages) {

                                    Badge(
                                        containerColor = colorAccent
                                    )
                                }
                            }
                        ) {

                            Icon(
                                Icons.Outlined.Email,
                                contentDescription = "Messages"
                            )
                        }
                    },

                    label = {
                        Text("Messages")
                    },

                    selected = false,

                    onClick = {

                        navController.navigate(ROUT_CLIENTMESSAGES) {
                            launchSingleTop = true
                        }
                    },

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

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDDE2E9)),
                    contentAlignment = Alignment.Center
                ) {

                    val validImage = remember(profileImage) {

                        profileImage.isNotBlank() &&
                                profileImage != "null" &&
                                (
                                        profileImage.startsWith("http") ||
                                                profileImage.startsWith("content://")
                                        )
                    }

                    if (validImage) {

                        AsyncImage(
                            model = profileImage,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                    } else {

                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.DarkGray,
                            modifier = Modifier.size(70.dp)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorPrimary
                )

                Text(
                    location,
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(32.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    ProfileMetricCard(
                        data = MetricData(
                            label = "Rating",
                            value = String.format("%.1f", rating),
                            icon = Icons.Default.Star,
                            color = Color(0xFFFFD700)
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    ProfileMetricCard(
                        data = MetricData(
                            label = "Jobs Posted",
                            value = jobsRequested.toString(),
                            icon = Icons.Default.History,
                            color = colorAccent
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(24.dp))
            }

            item {

                ProfileGroup(title = "Account Settings") {

                    ProfileItem(
                        text = "Personal Information",
                        icon = Icons.Outlined.Badge,
                        onClick = {
                            navController.navigate(ROUT_CLIENTEDITPROFILE)
                        }
                    )

                    ProfileItem(
                        text = "Payment Methods",
                        icon = Icons.Outlined.Payments,
                        onClick = {
                            navController.navigate(ROUT_PAYMENTMETHOD)
                        }
                    )
                }

                ProfileGroup(title = "Preferences") {

                    ProfileItem(
                        text = "Notifications",
                        icon = Icons.Outlined.Notifications,
                        onClick = {
                            navController.navigate(ROUT_CLIENTNOTIFICATION)
                        }
                    )
                }

                Column(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {

                    ProfileItem(
                        text = "Logout",
                        icon = Icons.Outlined.Logout,
                        iconColor = Color.Red,
                        onClick = {

                            FirebaseAuth.getInstance().signOut()

                            navController.navigate(ROUT_REGISTER) {

                                popUpTo(0)
                            }
                        }
                    )
                }

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

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

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(0.5.dp)
    ) {

        Column(

            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                data.icon,
                null,
                tint = data.color,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                data.value,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp
            )

            Text(
                data.label,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ProfileGroup(
    title: String,
    content: @Composable () -> Unit
) {

    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {

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
fun ProfileItem(
    text: String,
    icon: ImageVector,
    iconColor: Color = Color(0xFF1B263B),
    onClick: () -> Unit
) {

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(

            modifier = Modifier.padding(16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                icon,
                null,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )

            Spacer(Modifier.width(16.dp))

            Text(
                text = text,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                color =
                    if (iconColor == Color.Red)
                        Color.Red
                    else
                        Color(0xFF1B263B)
            )

            Icon(
                Icons.Default.ChevronRight,
                null,
                tint = Color.LightGray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClientProfileScreenPreview() {

    ClientProfileScreen(
        rememberNavController()
    )
}