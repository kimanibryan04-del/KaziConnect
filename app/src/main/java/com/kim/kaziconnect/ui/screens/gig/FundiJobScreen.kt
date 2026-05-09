package com.kim.kaziconnect.ui.screens.gig

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kim.kaziconnect.models.JobModel
import com.kim.kaziconnect.navigation.ROUT_CHAT
import com.kim.kaziconnect.navigation.ROUT_CLIENTPUBLICPROFILE
import com.kim.kaziconnect.navigation.ROUT_FUNDIHOME
import com.kim.kaziconnect.navigation.ROUT_FUNDIJOB
import com.kim.kaziconnect.navigation.ROUT_FUNDIMESSAGES
import com.kim.kaziconnect.navigation.ROUT_FUNDIPROFILE
import com.kim.kaziconnect.navigation.ROUT_JOBDETAILS_NOAPPLY
import com.kim.kaziconnect.navigation.ROUT_REVIEW

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundiJobScreen(navController: NavHostController) {

    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    val tabs = listOf(
        "Active",
        "Pending",
        "Completed"
    )

    val pendingJobs = remember {
        mutableStateListOf<JobModel>()
    }

    val activeJobs = remember {
        mutableStateListOf<JobModel>()
    }

    val completedJobs = remember {
        mutableStateListOf<JobModel>()
    }

    val userId =
        FirebaseAuth.getInstance().currentUser?.uid ?: ""

    var hasUnreadMessages by remember {
        mutableStateOf(false)
    }

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

                            receiverId == userId && !seen
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    // PENDING JOBS
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

                                    if (job != null &&
                                        job.status == "pending"
                                    ) {

                                        job.id = jobId

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

    // ACTIVE JOBS
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

                            if (job != null &&
                                job.status == "active"
                            ) {

                                job.id = jobSnapshot.key ?: ""

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

    // COMPLETED JOBS
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

                            if (job != null &&
                                job.status == "completed"
                            ) {

                                job.id = jobSnapshot.key ?: ""

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

    val jobsList = when (selectedTabIndex) {

        0 -> activeJobs

        1 -> pendingJobs

        else -> completedJobs
    }

    Scaffold(

        modifier = Modifier.statusBarsPadding(),

        topBar = {

            Column(
                modifier = Modifier.background(Color.White)
            ) {

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
                            Modifier.tabIndicatorOffset(
                                tabPositions[selectedTabIndex]
                            ),
                            color = Color(0xFF6750A4)
                        )
                    }
                ) {

                    tabs.forEachIndexed { index, title ->

                        Tab(
                            selected = selectedTabIndex == index,

                            onClick = {
                                selectedTabIndex = index
                            },

                            text = {

                                Text(
                                    text = title,

                                    color =
                                        if (selectedTabIndex == index)
                                            colorAccent
                                        else
                                            Color.Gray,

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

                    icon = {
                        Icon(
                            Icons.Outlined.Home,
                            contentDescription = null
                        )
                    },

                    label = {
                        Text("Home")
                    },

                    selected = false,

                    onClick = {
                        navController.navigate(ROUT_FUNDIHOME) {
                            launchSingleTop = true
                        }
                    }
                )

                NavigationBarItem(

                    icon = {
                        Icon(
                            Icons.Filled.List,
                            contentDescription = null
                        )
                    },

                    label = {
                        Text("My Jobs")
                    },

                    selected = true,

                    onClick = {
                        navController.navigate(ROUT_FUNDIJOB) {
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
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = null
                        )
                    },

                    label = {
                        Text("Profile")
                    },

                    selected = false,

                    onClick = {
                        navController.navigate(ROUT_FUNDIPROFILE) {
                            launchSingleTop = true
                        }
                    }
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
                                contentDescription = null
                            )
                        }
                    },

                    label = {
                        Text("Messages")
                    },

                    selected = false,

                    onClick = {
                        navController.navigate(ROUT_FUNDIMESSAGES) {
                            launchSingleTop = true
                        }
                    }
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

                val emptyMessage = when (selectedTabIndex) {

                    0 -> "No active jobs at the moment"

                    1 -> "You have not applied to any jobs yet"

                    else -> "No completed jobs yet"
                }

                EmptyJobsView(
                    text = emptyMessage
                )

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
                            navController = navController,

                            onClick = {

                                navController.navigate(
                                    "${ROUT_JOBDETAILS_NOAPPLY}/${job.id}"
                                )
                            },

                            onReviewClick = {

                                val database =
                                    FirebaseDatabase.getInstance().reference

                                database.child("users")
                                    .child(userId)
                                    .child("earnings")
                                    .get()

                                    .addOnSuccessListener { snapshot ->

                                        val currentEarnings =
                                            snapshot.getValue(Double::class.java)
                                                ?: 0.0

                                        val jobAmount =
                                            job.budget
                                                .replace("Ksh", "")
                                                .replace(",", "")
                                                .trim()
                                                .toDoubleOrNull() ?: 0.0

                                        val newEarnings =
                                            currentEarnings + jobAmount

                                        database.child("users")
                                            .child(userId)
                                            .child("earnings")
                                            .setValue(newEarnings)

                                        navController.navigate(
                                            "${ROUT_REVIEW}/${job.id}/${job.clientId}/fundi_to_client"
                                        )
                                    }
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
    navController: NavHostController,
    onClick: () -> Unit,
    onReviewClick: () -> Unit
) {

    val currentUserId =
        FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val chatId =
        if (currentUserId < job.clientId)
            "${currentUserId}_${job.clientId}"
        else
            "${job.clientId}_${currentUserId}"

    val jobIcon = when (job.category) {

        "Plumber" -> Icons.Default.Build

        "Electrician" -> Icons.Default.Bolt

        "Painter" -> Icons.Default.Brush

        "Cleaner" -> Icons.Default.CleaningServices

        "Carpenter" -> Icons.Default.Handyman

        "Mason" -> Icons.Default.HomeRepairService

        else -> Icons.Default.Work
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        shape = RoundedCornerShape(16.dp),

        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = colorAccent.copy(alpha = 0.12f)
                ) {

                    Box(
                        modifier = Modifier.size(56.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = jobIcon,
                            contentDescription = null,
                            tint = colorAccent,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {

                    Text(
                        text = job.title,
                        fontWeight = FontWeight.Bold,
                        color = colorPrimary,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = job.location,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = job.budget,
                        fontWeight = FontWeight.Black,
                        color = colorAccent
                    )
                }
            }

            /*
            SHOW ONLY IN COMPLETED JOBS
             */
            if (job.status == "completed") {

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedButton(

                    onClick = {

                        navController.navigate(
                            "$ROUT_CLIENTPUBLICPROFILE/${job.clientId}"
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
                        text = "View Client Profile",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (!job.fundiReviewed) {

                    Button(
                        onClick = {
                            onReviewClick()
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
                            text = "Review Client",
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
                            text = "✅ You already reviewed this client",

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
                            if (job.clientReviewed)
                                Color(0xFFE8F5E9)
                            else
                                Color(0xFFFFF3E0)
                    )
                ) {

                    Text(
                        text =
                            if (job.clientReviewed)
                                "⭐ Client has reviewed you"
                            else
                                "⏳ Waiting for client review",

                        modifier = Modifier.padding(12.dp),

                        color =
                            if (job.clientReviewed)
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

@Composable
fun EmptyJobsView(text: String) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.LightGray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = text,
            color = Color.Gray,
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FundiJobScreenPreview() {

    FundiJobScreen(
        rememberNavController()
    )
}