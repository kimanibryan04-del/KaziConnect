package com.kim.kaziconnect.ui.screens.gig

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.kim.kaziconnect.models.JobModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kim.kaziconnect.navigation.ROUT_JOBDETAILS
import com.kim.kaziconnect.navigation.ROUT_JOBDETAILS_NOAPPLY

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundiJobScreen(navController: NavHostController) {
    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Active", "Pending", "Completed")

    val pendingJobs = remember { mutableStateListOf<JobModel>() }

    val activeJobs = remember { mutableStateListOf<JobModel>() }

    val completedJobs = remember { mutableStateListOf<JobModel>() }

    val jobsList = when (selectedTabIndex) {

        0 -> activeJobs

        1 -> pendingJobs

        else -> completedJobs
    }

    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    LaunchedEffect(Unit) {

        FirebaseDatabase.getInstance().reference
            .child("applications")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    pendingJobs.clear()

                    for (jobSnapshot in snapshot.children) {

                        if (jobSnapshot.hasChild(userId)) {

                            val jobId = jobSnapshot.key ?: ""

                            FirebaseDatabase.getInstance().reference
                                .child("jobs")
                                .child(jobId)
                                .get()
                                .addOnSuccessListener { jobData ->

                                    val job =
                                        jobData.getValue(JobModel::class.java)

                                    if (job != null) {

                                        pendingJobs.add(job)
                                    }
                                }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    LaunchedEffect(Unit) {

        FirebaseDatabase.getInstance().reference
            .child("ongoingJobs")
            .child(userId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    activeJobs.clear()

                    for (jobSnapshot in snapshot.children) {

                        try {

                            val job =
                                jobSnapshot.getValue(JobModel::class.java)

                            if (job != null) {

                                activeJobs.add(job)
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

    LaunchedEffect(Unit) {

        FirebaseDatabase.getInstance().reference
            .child("completedJobs")
            .child(userId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    completedJobs.clear()

                    for (jobSnapshot in snapshot.children) {

                        try {

                            val job =
                                jobSnapshot.getValue(JobModel::class.java)

                            if (job != null) {

                                completedJobs.add(job)
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

    Scaffold(
        modifier = Modifier.statusBarsPadding(), // Added: Pushes the dashboard title below the system clock/status bar
        topBar = {
            Column(modifier = Modifier.background(Color.White)) {
                Text(
                    text = "My Jobs Dashboard",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = colorPrimary
                )
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.White,
                    contentColor = colorAccent,
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = Color(0xFF6750A4)
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    color = if (selectedTabIndex == index) colorAccent else Color.Gray,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
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
                    icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
                    label = { Text("Home") },
                    selected = false,
                    onClick = {navController.navigate(ROUT_FUNDIHOME) {
                        launchSingleTop = true
                    } }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = null) },
                    label = { Text("My Jobs") },
                    selected = true,
                    onClick = { navController.navigate(ROUT_FUNDIJOB) {
                        launchSingleTop = true
                    } },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        selectedTextColor = colorAccent,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = {navController.navigate(ROUT_FUNDIPROFILE) {
                        launchSingleTop = true
                    } }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                    label = { Text("Messages") },
                    selected = false,
                    onClick = {navController.navigate(ROUT_FUNDIMESSAGES) {
                        launchSingleTop = true
                    } }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(lightBg),
            contentAlignment = Alignment.Center
        ) {
            if (jobsList.isEmpty()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "You have no ${tabs[selectedTabIndex].lowercase()} jobs",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(jobsList) { job ->

                        JobCard(
                            job = job,
                            colorPrimary = colorPrimary,
                            colorAccent = colorAccent,
                            onClick = {
                                navController.navigate("${ROUT_JOBDETAILS_NOAPPLY}/${job.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun JobCard(
    job: JobModel,
    colorPrimary: Color,
    colorAccent: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(job.title ?: "", fontWeight = FontWeight.Bold, color = colorPrimary)
            Text(job.location ?: "", color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(job.budget ?: "", fontWeight = FontWeight.Black, color = colorAccent)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FundiJobScreenPreview() {
    FundiJobScreen(rememberNavController())
}