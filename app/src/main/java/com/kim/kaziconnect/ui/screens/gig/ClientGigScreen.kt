package com.kim.kaziconnect.ui.screens.gig

import android.R.attr.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.kim.kaziconnect.navigation.ROUT_CLIENTGIG
import com.kim.kaziconnect.navigation.ROUT_CLIENTHOME
import com.kim.kaziconnect.navigation.ROUT_CLIENTMESSAGES
import com.kim.kaziconnect.navigation.ROUT_CLIENTPROFILE
import com.kim.kaziconnect.navigation.ROUT_POSTJOB
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kim.kaziconnect.models.JobModel
import com.kim.kaziconnect.navigation.ROUT_APPLICANTSLIST

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientGigScreen(navController: NavHostController) {

    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        "Active",
        "Pending",
        "Completed"
    )

    val activeJobs = remember { mutableStateListOf<JobModel>() }

    val pendingJobs = remember { mutableStateListOf<JobModel>() }

    val completedJobs = remember { mutableStateListOf<JobModel>() }


    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

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
                        Icon(
                            Icons.Outlined.Email,
                            contentDescription = "Messages"
                        )
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

                        Card(

                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {

                                    navController.navigate(
                                        "${ROUT_APPLICANTSLIST}/${job.id}"
                                    )
                                },




                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),

                            shape = RoundedCornerShape(16.dp)

                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

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
                            }
                        }
                    }
                }
            }



            FloatingActionButton(

                onClick = {

                    navController.navigate(ROUT_POSTJOB)

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