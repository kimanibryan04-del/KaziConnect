package com.kim.kaziconnect.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.kim.kaziconnect.models.FundiStats
import com.kim.kaziconnect.models.JobModel
import com.kim.kaziconnect.navigation.*
import com.kim.kaziconnect.navigation.ROUT_CLIENTPUBLICPROFILE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundiHomeScreen(navController: NavHostController) {

    var stats by remember {
        mutableStateOf(FundiStats())
    }

    val database = FirebaseDatabase.getInstance().reference

    /*
    NOTIFICATION BADGE
     */
    var unreadCount by remember {
        mutableStateOf(0)
    }

    /*
    MESSAGE BADGE
     */
    var hasUnreadMessages by remember {
        mutableStateOf(false)
    }

    /*
    FUNDI SKILL
     */
    var fundiSkill by remember {
        mutableStateOf("")
    }

    /*
    LISTEN FOR UNREAD MESSAGES
     */
    LaunchedEffect(Unit) {

        val userId =
            FirebaseAuth.getInstance().currentUser?.uid
                ?: return@LaunchedEffect

        database.child("chats")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    hasUnreadMessages = false

                    for (chatSnapshot in snapshot.children) {

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

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    /*
    NOTIFICATIONS
     */
    LaunchedEffect(Unit) {

        val userId =
            FirebaseAuth.getInstance().currentUser?.uid
                ?: return@LaunchedEffect

        database.child("notifications")
            .child(userId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    try {

                        var count = 0

                        for (notificationSnapshot in snapshot.children) {

                            val read =
                                notificationSnapshot
                                    .child("read")
                                    .getValue(Boolean::class.java)
                                    ?: false

                            if (!read) {
                                count++
                            }
                        }

                        unreadCount = count

                    } catch (e: Exception) {

                        e.printStackTrace()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    /*
    FUNDI STATS
     */
    LaunchedEffect(Unit) {

        val userId =
            FirebaseAuth.getInstance().currentUser?.uid
                ?: return@LaunchedEffect

        database.child("users")
            .child(userId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    try {

                        stats = FundiStats(

                            earnings =
                                snapshot.child("earnings")
                                    .getValue(Double::class.java)
                                    ?: 0.0,

                            rating =
                                snapshot.child("rating")
                                    .getValue(Float::class.java)
                                    ?.toDouble()
                                    ?: 0.0,

                            completedJobs =
                                snapshot.child("completedJobs")
                                    .getValue(Int::class.java)
                                    ?: 0
                        )

                    } catch (e: Exception) {

                        e.printStackTrace()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
    /*
    GET FUNDI SKILL
     */
    LaunchedEffect(Unit) {

        val userId =
            FirebaseAuth.getInstance().currentUser?.uid
                ?: return@LaunchedEffect

        database.child("users")
            .child(userId)
            .child("skill")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    fundiSkill =
                        snapshot.getValue(String::class.java)
                            ?: ""
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    val availableJobs = remember {
        mutableStateListOf<JobModel>()
    }

    val ongoingTasks = remember {
        mutableStateListOf<JobModel>()
    }

    var searchText by remember {
        mutableStateOf("")
    }

    /*
    UPDATED SEARCH FILTER
     */
    val filteredJobs = availableJobs.filter { job ->

        job.title.contains(
            searchText,
            ignoreCase = true
        )

                ||

                job.location.contains(
                    searchText,
                    ignoreCase = true
                )

                ||

                job.category.contains(
                    searchText,
                    ignoreCase = true
                )
    }

    /*
    AVAILABLE JOBS
     */
    LaunchedEffect(fundiSkill) {

        database.child("jobs")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    availableJobs.clear()

                    for (jobSnapshot in snapshot.children) {

                        try {

                            val job =
                                jobSnapshot.getValue(JobModel::class.java)

                            if (job != null) {

                                job.id = jobSnapshot.key ?: ""

                                /*
                                IF NO SKILL -> SHOW ALL JOBS
                                 */
                                val currentUserId =
                                    FirebaseAuth.getInstance().currentUser?.uid ?: ""

                                /*
                                IF NO SKILL -> SHOW ALL JOBS
                                 */
                                if (fundiSkill.isBlank()) {

                                    if (job.fundiId != currentUserId) {

                                        availableJobs.add(job)
                                    }
                                }

                                /*
                                FILTER JOBS BY SKILL
                                 */
                                else if (

                                    job.category.equals(
                                        fundiSkill,
                                        ignoreCase = true
                                    )
                                ) {

                                    if (job.fundiId != currentUserId) {

                                        availableJobs.add(job)
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

    /*
    ONGOING TASKS
     */
    LaunchedEffect(Unit) {

        val userId =
            FirebaseAuth.getInstance().currentUser?.uid
                ?: return@LaunchedEffect

        database.child("ongoingJobs")
            .child(userId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    try {

                        ongoingTasks.clear()

                        for (jobSnapshot in snapshot.children) {

                            val job =
                                jobSnapshot.getValue(JobModel::class.java)

                            if (job != null) {

                                ongoingTasks.add(job)
                            }
                        }

                    } catch (e: Exception) {

                        e.printStackTrace()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    Scaffold(

        modifier = Modifier.statusBarsPadding(),

        bottomBar = {

            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {

                NavigationBarItem(

                    icon = {

                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Home"
                        )
                    },

                    label = {
                        Text("Home")
                    },

                    selected = true,

                    onClick = {

                        navController.navigate(ROUT_FUNDIHOME) {
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
                            Icons.Filled.List,
                            contentDescription = "My Jobs"
                        )
                    },

                    label = {
                        Text("My Jobs")
                    },

                    selected = false,

                    onClick = {

                        navController.navigate(ROUT_FUNDIJOB) {
                            launchSingleTop = true
                        }
                    }
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
                                contentDescription = "Messages"
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
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        selectedTextColor = colorAccent,
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
                .background(lightBg)
                .padding(horizontal = 20.dp)

        ) {

            item {

                Spacer(modifier = Modifier.height(24.dp))

                Row(

                    modifier = Modifier.fillMaxWidth(),

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(

                        onClick = {

                            navController.navigate(
                                route = ROUT_REGISTER
                            )
                        },

                        modifier = Modifier
                            .background(
                                Color.White,
                                CircleShape
                            )
                            .size(40.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = colorPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Work Dashboard",
                            fontSize = 24.sp,
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
                                    ROUT_FUNDINOTIFICATION
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
                                    Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = colorPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Surface(

                    shape = RoundedCornerShape(16.dp),

                    color = Color.White,

                    shadowElevation = 2.dp,

                    modifier = Modifier.fillMaxWidth()
                ) {

                    OutlinedTextField(

                        value = searchText,

                        onValueChange = {
                            searchText = it
                        },

                        placeholder = {

                            Text(
                                "Search for jobs",
                                fontSize = 14.sp
                            )
                        },

                        leadingIcon = {

                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = colorAccent
                            )
                        },

                        modifier = Modifier.fillMaxWidth(),

                        singleLine = true,

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Your Performance",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = colorPrimary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(

                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    StatCard(
                        title = "Earnings",
                        value = "KES ${stats.earnings}",
                        icon = Default.Payments,
                        iconColor = colorAccent,
                        modifier = Modifier.weight(1f)
                    )

                    StatCard(
                        title = "Rating",
                        value = stats.rating.toString(),
                        icon = Default.Star,
                        iconColor = Color(0xFFFFD700),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            item {

                Text(
                    text = "Ongoing Tasks",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = colorPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (ongoingTasks.isEmpty()) {

                item {

                    Card(

                        modifier = Modifier.fillMaxWidth(),

                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),

                        shape = RoundedCornerShape(20.dp)
                    ) {

                        Box(

                            modifier = Modifier
                                .padding(30.dp)
                                .fillMaxWidth(),

                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = "No active tasks",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }

            } else {

                items(ongoingTasks) { task ->

                    Card(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),

                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),

                        shape = RoundedCornerShape(16.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {

                            Row(

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                        navController.navigate(
                                            "${ROUT_JOBDETAILS_NOAPPLY}/${task.id}"
                                        )
                                    },

                                verticalAlignment = Alignment.CenterVertically,

                                horizontalArrangement =
                                    Arrangement.SpaceBetween
                            ) {

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = task.title,
                                        fontWeight = FontWeight.Bold,
                                        color = colorPrimary,
                                        fontSize = 16.sp
                                    )

                                    Text(
                                        text = task.location,
                                        color = Color.Gray,
                                        fontSize = 13.sp
                                    )
                                }

                                Text(
                                    text = task.budget,
                                    fontWeight = FontWeight.Black,
                                    color = colorAccent,
                                    fontSize = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {

                                Button(

                                    onClick = {

                                        val currentUserId =
                                            FirebaseAuth.getInstance().currentUser?.uid ?: ""

                                        val chatId =
                                            if (currentUserId < task.clientId)
                                                "${currentUserId}_${task.clientId}"
                                            else
                                                "${task.clientId}_${currentUserId}"

                                        navController.navigate(
                                            "$ROUT_CHAT/$chatId/${task.clientId}/${task.clientName}"
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
                                        text = "Message Client",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                OutlinedButton(

                                    onClick = {

                                        navController.navigate(
                                            "$ROUT_CLIENTPUBLICPROFILE/${task.clientId}"
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
                            }
                        }
                    }
                }
            }

            item {

                Text(
                    text = "Available Jobs",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = colorPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (filteredJobs.isEmpty()) {

                item {

                    Card(

                        modifier = Modifier.fillMaxWidth(),

                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),

                        shape = RoundedCornerShape(20.dp)
                    ) {

                        Column(

                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(30.dp),

                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Icon(
                                imageVector = Icons.Default.WorkOff,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(60.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "No jobs yet",
                                color = Color.Gray,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Jobs posted by clients will appear here",
                                color = Color.LightGray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

            } else {

                items(filteredJobs) { job ->

                    Card(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {

                                navController.navigate(
                                    "${ROUT_JOBDETAILS}/${job.id}"
                                )
                            },

                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),

                        shape = RoundedCornerShape(16.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {

                            Row(

                                verticalAlignment = Alignment.CenterVertically,

                                horizontalArrangement =
                                    Arrangement.SpaceBetween,

                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = job.title,
                                        fontWeight = FontWeight.Bold,
                                        color = colorPrimary,
                                        fontSize = 16.sp
                                    )

                                    Text(
                                        text = job.location,
                                        color = Color.Gray,
                                        fontSize = 13.sp
                                    )
                                }

                                Text(
                                    text = job.budget,
                                    fontWeight = FontWeight.Black,
                                    color = colorAccent,
                                    fontSize = 16.sp
                                )
                            }

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
                        }
                    }
                }
            }

            item {

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier
) {

    Surface(

        modifier = modifier,

        shape = RoundedCornerShape(20.dp),

        color = Color.White,

        shadowElevation = 4.dp
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                color = Color.Gray,
                fontSize = 12.sp
            )

            Text(
                text = value,
                color = Color(0xFF1B263B),
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FundiHomeScreenPreview() {

    FundiHomeScreen(
        rememberNavController()
    )
}