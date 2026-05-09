package com.kim.kaziconnect.ui.screens.gig

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kim.kaziconnect.models.JobModel
import com.kim.kaziconnect.navigation.ROUT_APPLICANTSLIST
import com.kim.kaziconnect.navigation.ROUT_CHAT
import com.kim.kaziconnect.navigation.ROUT_CLIENTGIG
import com.kim.kaziconnect.navigation.ROUT_CLIENTHOME
import com.kim.kaziconnect.navigation.ROUT_CLIENTMESSAGES
import com.kim.kaziconnect.navigation.ROUT_CLIENTPROFILE
import com.kim.kaziconnect.navigation.ROUT_JOBDETAILS_NOAPPLY
import com.kim.kaziconnect.navigation.ROUT_POSTJOB
import com.kim.kaziconnect.navigation.ROUT_REVIEW
import com.kim.kaziconnect.navigation.ROUT_FUNDIPUBLICPROFILE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientGigScreen(navController: NavHostController) {

    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    val tabs = listOf(
        "Active",
        "Pending",
        "Completed"
    )

    val activeJobs = remember {
        mutableStateListOf<JobModel>()
    }

    val pendingJobs = remember {
        mutableStateListOf<JobModel>()
    }

    val completedJobs = remember {
        mutableStateListOf<JobModel>()
    }

    var hasUnreadMessages by remember {
        mutableStateOf(false)
    }

    val currentUserId =
        FirebaseAuth.getInstance().currentUser?.uid ?: ""

    LaunchedEffect(Unit) {

        FirebaseDatabase.getInstance().reference
            .child("messages")
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

                            receiverId == currentUserId && !seen
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    val userId =
        FirebaseAuth.getInstance().currentUser?.uid ?: ""

    LaunchedEffect(Unit) {

        FirebaseDatabase.getInstance().reference
            .child("jobs")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    activeJobs.clear()
                    pendingJobs.clear()
                    completedJobs.clear()

                    for (jobSnapshot in snapshot.children) {

                        try {

                            val job =
                                jobSnapshot.getValue(JobModel::class.java)

                            if (job != null) {

                                job.id = jobSnapshot.key ?: ""

                                if (job.clientId == userId) {

                                    when (job.status) {

                                        "pending" -> {
                                            pendingJobs.add(job)
                                        }

                                        "active" -> {
                                            activeJobs.add(job)
                                        }

                                        "completed" -> {
                                            completedJobs.add(job)
                                        }
                                    }
                                }
                            }

                        } catch (e: Exception) {

                            e.printStackTrace()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    val jobsList = when (selectedTab) {

        0 -> activeJobs

        1 -> pendingJobs

        else -> completedJobs
    }

    Scaffold(

        topBar = {

            Column(
                modifier = Modifier.background(Color.White)
            ) {

                CenterAlignedTopAppBar(

                    title = {

                        Text(
                            text = "My Gigs",
                            fontWeight = FontWeight.Black,
                            color = colorPrimary
                        )
                    },

                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    )
                )

                SecondaryTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = colorAccent
                ) {

                    tabs.forEachIndexed { index, title ->

                        Tab(
                            selected = selectedTab == index,

                            onClick = {
                                selectedTab = index
                            },

                            text = {

                                Text(
                                    text = title,

                                    fontWeight =
                                        if (selectedTab == index)
                                            FontWeight.Bold
                                        else
                                            FontWeight.Medium,

                                    color =
                                        if (selectedTab == index)
                                            colorAccent
                                        else
                                            Color.Gray
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

                    selected = true,

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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(lightBg)
        ) {

            if (jobsList.isEmpty()) {

                when (selectedTab) {

                    0 -> EmptyStateView(
                        "You have no active gigs",
                        Icons.Default.Search
                    )

                    1 -> EmptyStateView(
                        "No pending requests",
                        Icons.Default.Info
                    )

                    2 -> EmptyStateView(
                        "No past history",
                        Icons.Default.History
                    )
                }

            } else {

                LazyColumn(

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),

                    verticalArrangement = Arrangement.spacedBy(12.dp)

                ) {

                    items(jobsList) { job ->

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
                                .clickable {

                                    if (selectedTab == 1) {

                                        navController.navigate(
                                            "${ROUT_APPLICANTSLIST}/${job.id}"
                                        )

                                    } else {

                                        navController.navigate(
                                            "${ROUT_JOBDETAILS_NOAPPLY}/${job.id}"
                                        )
                                    }
                                },

                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),

                            shape = RoundedCornerShape(16.dp)

                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

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
                                    fontSize = 16.sp,
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
                                    fontWeight = FontWeight.Black
                                )

                                /*
                                MESSAGE BUTTON ONLY FOR ACTIVE TAB
                                 */
                                if (selectedTab == 0) {

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Button(

                                        onClick = {

                                            val participants =
                                                listOf(
                                                    currentUserId,
                                                    job.fundiId
                                                ).sorted()

                                            val chatId =
                                                participants.joinToString("_")


                                            navController.navigate(
                                                "$ROUT_CHAT/$chatId/${job.fundiId}/${job.fundiName}"
                                            )
                                        },

                                        modifier = Modifier.fillMaxWidth(),

                                        shape = RoundedCornerShape(14.dp),

                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = colorAccent
                                        )
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

                                if (job.status == "completed") {

                                    Spacer(modifier = Modifier.height(14.dp))

                                    OutlinedButton(

                                        onClick = {

                                            navController.navigate(
                                                "$ROUT_FUNDIPUBLICPROFILE/${job.fundiId}"
                                            )
                                        },

                                        modifier = Modifier.fillMaxWidth(),

                                        shape = RoundedCornerShape(14.dp),

                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = colorAccent
                                        )
                                    ) {

                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(
                                            text = "View Profile",
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    if (!job.clientReviewed) {

                                        Button(

                                            onClick = {

                                                navController.navigate(
                                                    "${ROUT_REVIEW}/${job.id}/${job.fundiId}/client_to_fundi"
                                                )
                                            },

                                            modifier = Modifier.fillMaxWidth(),

                                            shape = RoundedCornerShape(14.dp),

                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = colorAccent
                                            )
                                        ) {

                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = Color.White
                                            )

                                            Spacer(modifier = Modifier.width(8.dp))

                                            Text(
                                                text = "Review Fundi",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                    } else {

                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(0xFFE8F5E9)
                                            )
                                        ) {

                                            Text(
                                                text = "✅ You already reviewed this fundi",

                                                modifier = Modifier.padding(12.dp),

                                                color = Color(0xFF2E7D32),

                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor =
                                                if (job.fundiReviewed)
                                                    Color(0xFFE8F5E9)
                                                else
                                                    Color(0xFFFFF3E0)
                                        )
                                    ) {

                                        Text(
                                            text =
                                                if (job.fundiReviewed)
                                                    "⭐ Fundi has reviewed you"
                                                else
                                                    "⏳ Waiting for fundi review",

                                            modifier = Modifier.padding(12.dp),

                                            color =
                                                if (job.fundiReviewed)
                                                    Color(0xFF2E7D32)
                                                else
                                                    Color(0xFFE65100),

                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            FloatingActionButton(

                onClick = {

                    navController.navigate("$ROUT_POSTJOB/")

                },

                containerColor = colorAccent,
                contentColor = Color.White,

                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
                    .size(60.dp)

            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Post Job",
                    modifier = Modifier.size(34.dp)
                )
            }

            Surface(

                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = 24.dp,
                        bottom = 105.dp
                    ),

                shape = RoundedCornerShape(12.dp),

                shadowElevation = 3.dp,

                color = Color.White

            ) {

                Text(
                    text = "Post Job",

                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 8.dp
                    ),

                    color = colorPrimary,

                    fontWeight = FontWeight.SemiBold,

                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun EmptyStateView(
    message: String,
    icon: ImageVector
) {

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

        Spacer(modifier = Modifier.height(16.dp))

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

    ClientGigScreen(
        rememberNavController()
    )
}