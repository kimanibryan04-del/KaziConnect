package com.kim.kaziconnect.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kim.kaziconnect.models.JobModel
import com.kim.kaziconnect.navigation.ROUT_CHAT
import com.kim.kaziconnect.navigation.ROUT_CLIENTGIG
import com.kim.kaziconnect.navigation.ROUT_CLIENTHOME
import com.kim.kaziconnect.navigation.ROUT_CLIENTMESSAGES
import com.kim.kaziconnect.navigation.ROUT_CLIENTNOTIFICATION
import com.kim.kaziconnect.navigation.ROUT_CLIENTPROFILE
import com.kim.kaziconnect.navigation.ROUT_FUNDIPUBLICPROFILE
import com.kim.kaziconnect.navigation.ROUT_JOBDETAILS_NOAPPLY
import com.kim.kaziconnect.navigation.ROUT_POSTJOB
import com.kim.kaziconnect.navigation.ROUT_REGISTER

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientHomeScreen(navController: NavHostController) {

    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().reference

    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    var searchText by remember {
        mutableStateOf("")
    }

    var hasUnreadMessages by remember {
        mutableStateOf(false)
    }

    var unreadCount by remember {
        mutableStateOf(0)
    }

    val ongoingJobs = remember {
        mutableStateListOf<JobModel>()
    }

    val userId =
        auth.currentUser?.uid ?: ""

    LaunchedEffect(Unit) {

        database.child("notifications")
            .child(userId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    var count = 0

                    for (notificationSnapshot in snapshot.children) {

                        val read = notificationSnapshot
                            .child("read")
                            .getValue(Boolean::class.java) ?: false

                        if (!read) {
                            count++
                        }
                    }

                    unreadCount = count
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    LaunchedEffect(Unit) {

        database.child("messages")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    hasUnreadMessages = snapshot.children.any { chatSnapshot ->

                        chatSnapshot.children.any { messageSnapshot ->

                            val receiverId =
                                messageSnapshot.child("receiverId")
                                    .getValue(String::class.java) ?: ""

                            val seen =
                                messageSnapshot.child("seen")
                                    .getValue(Boolean::class.java) ?: false

                            receiverId == userId && !seen
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    LaunchedEffect(Unit) {

        database.child("clientOngoingJobs")
            .child(userId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    ongoingJobs.clear()

                    for (jobSnapshot in snapshot.children) {

                        val job =
                            jobSnapshot.getValue(JobModel::class.java)

                        if (job != null) {

                            ongoingJobs.add(job)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    Scaffold(

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

                    selected = true,

                    onClick = {

                        navController.navigate(ROUT_CLIENTHOME) {
                            launchSingleTop = true
                        }
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        selectedTextColor = colorAccent,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
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
                        selectedTextColor = colorAccent,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(

                    icon = {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = "Profile"
                        )
                    },

                    label = {
                        Text("Profile")
                    },

                    selected = false,

                    onClick = {

                        navController.navigate(ROUT_CLIENTPROFILE) {
                            launchSingleTop = true
                        }
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        selectedTextColor = colorAccent,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        navController.navigate(route = ROUT_REGISTER)
                    },
                    modifier = Modifier
                        .background(Color.White, CircleShape)
                        .size(40.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = colorPrimary
                    )
                }

                Column {

                    Text(
                        text = "Find Help Nearby",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = colorPrimary
                    )

                }

                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {

                    IconButton(
                        onClick = {
                            navController.navigate(
                                ROUT_CLIENTNOTIFICATION
                            )
                        }
                    ) {

                        BadgedBox(

                            badge = {

                                if (unreadCount > 0) {

                                    Badge(
                                        containerColor = colorAccent
                                    ) {

                                        Text(
                                            text = unreadCount.toString(),
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                        ) {

                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = colorPrimary,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {

                OutlinedTextField(
                    value = searchText,

                    onValueChange = {
                        searchText = it
                    },

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
                            tint = colorAccent
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
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = colorAccent
                    ),

                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                "Choose a service and get started",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = colorPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            val services = listOf(
                Triple("Plumber", Default.Build, Color(0xFFE3F2FD)),
                Triple("Electrician", Default.Bolt, Color(0xFFFFF3E0)),
                Triple("Mason", Default.Tapas, Color(0xFFE8F5E9)),
                Triple("Painter", Default.Brush, Color(0xFFF3E5F5)),
                Triple("Carpenter", Default.Hardware, Color(0xFFEFEBE9)),
                Triple("Cleaner", Default.LocalLaundryService, Color(0xFFFFEBEE))
            )

            val filteredServices = services.filter {

                it.first.contains(
                    searchText,
                    ignoreCase = true
                )
            }

            filteredServices.chunked(2).forEach { rowItems ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),

                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    rowItems.forEach { (name, icon, bg) ->

                        ServiceCard(
                            name = name,
                            icon = icon,
                            iconBg = bg,
                            primaryColor = colorPrimary,
                            modifier = Modifier.weight(1f),

                            onClick = {

                                navController.navigate(
                                    "$ROUT_POSTJOB/$name"
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "My Ongoing Tasks",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = colorPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (ongoingJobs.isEmpty()) {

                Card(
                    modifier = Modifier.fillMaxWidth(),

                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),

                    shape = RoundedCornerShape(20.dp),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {

                    Box(
                        modifier = Modifier
                            .padding(40.dp)
                            .fillMaxWidth(),

                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            "No active tasks found",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }

            } else {

                ongoingJobs.forEach { job ->

                    val jobIcon = when (job.category) {

                        "Plumber" -> Icons.Default.Build
                        "Electrician" -> Icons.Default.Bolt
                        "Painter" -> Icons.Default.Brush
                        "Mason" -> Icons.Default.Tapas
                        "Carpenter" -> Icons.Default.Hardware
                        "Cleaner" -> Icons.Default.LocalLaundryService

                        else -> Icons.Default.Work
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),

                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),

                        shape = RoundedCornerShape(20.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(18.dp)
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                        navController.navigate(
                                            "${ROUT_JOBDETAILS_NOAPPLY}/${job.id}"
                                        )
                                    }
                            ) {

                                Column {

                                    Icon(
                                        imageVector = jobIcon,
                                        contentDescription = null,
                                        tint = colorAccent,
                                        modifier = Modifier.size(28.dp)
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = job.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp,
                                        color = colorPrimary
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = job.location,
                                        color = Color.Gray,
                                        fontSize = 13.sp
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = job.budget,
                                        color = colorAccent,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedButton(

                                onClick = {

                                    navController.navigate(
                                        "$ROUT_FUNDIPUBLICPROFILE/${job.fundiId}"
                                    )
                                },

                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = colorAccent
                                ),

                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = SolidColor(colorAccent)
                                )
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Text("View Profile")
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(

                                onClick = {

                                    val currentUserId =
                                        FirebaseAuth.getInstance()
                                            .currentUser?.uid ?: ""

                                    val fundiId =
                                        job.fundiId

                                    val fundiName =
                                        job.fundiName

                                    val chatId =
                                        if (currentUserId < fundiId) {
                                            "${currentUserId}_${fundiId}"
                                        } else {
                                            "${fundiId}_${currentUserId}"
                                        }

                                    navController.navigate(
                                        "${ROUT_CHAT}/$chatId/${job.fundiId}/${job.fundiName}"
                                    )
                                },

                                modifier = Modifier.fillMaxWidth(),

                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorAccent
                                ),

                                shape = RoundedCornerShape(14.dp)
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    tint = Color.White
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "Message Fundi",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ServiceCard(
    name: String,
    icon: ImageVector,
    iconBg: Color,
    primaryColor: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {

    Surface(
        modifier = modifier
            .aspectRatio(1f)
            .clickable {
                onClick()
            },

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
                modifier = Modifier
                    .size(56.dp)
                    .background(iconBg, CircleShape),

                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClientHomeScreenPreview() {

    ClientHomeScreen(
        rememberNavController()
    )
}