package com.kim.kaziconnect.ui.screens.jobdetails

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kim.kaziconnect.models.JobModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailsScreen(
    navController: NavHostController,
    jobId: String,
    showApplyButton: Boolean = true
) {

    var hasApplied by remember {
        mutableStateOf(false)
    }

    var isCompleted by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    // COLORS
    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    // JOB DATA
    var jobTitle by remember {
        mutableStateOf("")
    }

    var location by remember {
        mutableStateOf("")
    }

    var budget by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var category by remember {
        mutableStateOf("")
    }

    var clientId by remember {
        mutableStateOf("")
    }

    var fundiId by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {

        val userId =
            FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // CHECK IF USER HAS APPLIED
        FirebaseDatabase.getInstance().reference
            .child("applications")
            .child(jobId)
            .child(userId)
            .get()
            .addOnSuccessListener {

                hasApplied = it.exists()
            }

        // LOAD JOB DETAILS
        FirebaseDatabase.getInstance().reference
            .child("jobs")
            .child(jobId)
            .get()
            .addOnSuccessListener { snapshot ->

                jobTitle =
                    snapshot.child("title")
                        .getValue(String::class.java) ?: ""

                location =
                    snapshot.child("location")
                        .getValue(String::class.java) ?: ""

                budget =
                    snapshot.child("budget")
                        .getValue(String::class.java) ?: ""

                description =
                    snapshot.child("description")
                        .getValue(String::class.java) ?: ""

                category =
                    snapshot.child("category")
                        .getValue(String::class.java) ?: ""

                clientId =
                    snapshot.child("clientId")
                        .getValue(String::class.java) ?: ""

                fundiId =
                    snapshot.child("fundiId")
                        .getValue(String::class.java) ?: ""

                val status =
                    snapshot.child("status")
                        .getValue(String::class.java) ?: ""

                isCompleted = status == "completed"
            }
    }

    // CATEGORY ICON
    val categoryIcon = when (category) {

        "Plumbing" -> Icons.Default.Build

        "Electrical" -> Icons.Default.Bolt

        "Masonry" -> Icons.Default.HomeRepairService

        "Painting" -> Icons.Default.Brush

        "Carpentry" -> Icons.Default.Handyman

        "Cleaning" -> Icons.Default.CleaningServices

        else -> Icons.Default.Work
    }

    Scaffold(
        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Job Details",
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(lightBg)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            // HEADER CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(22.dp)
                ) {

                    Surface(
                        shape = CircleShape,
                        color = colorAccent.copy(alpha = 0.12f)
                    ) {

                        Box(
                            modifier = Modifier.size(62.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = categoryIcon,
                                contentDescription = null,
                                tint = colorAccent,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = jobTitle,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = colorPrimary
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = colorAccent,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = location,
                            color = Color.Gray,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.Payments,
                            contentDescription = null,
                            tint = colorAccent,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = budget,
                            color = colorAccent,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // DESCRIPTION TITLE
            Text(
                text = "Job Description",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = colorPrimary
            )

            Spacer(modifier = Modifier.height(14.dp))

            // DESCRIPTION CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Text(
                    text = description,
                    modifier = Modifier.padding(20.dp),
                    color = Color.Gray,
                    fontSize = 15.sp,
                    lineHeight = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // APPLY BUTTON
            if (!hasApplied && showApplyButton) {

                Button(

                    onClick = {

                        val userId =
                            FirebaseAuth.getInstance().currentUser?.uid ?: ""

                        FirebaseDatabase.getInstance().reference
                            .child("applications")
                            .child(jobId)
                            .child(userId)
                            .setValue(true)
                            .addOnSuccessListener {

                                hasApplied = true

                                Toast.makeText(
                                    context,
                                    "Application sent",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),

                    shape = RoundedCornerShape(18.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorAccent
                    )
                ) {

                    Text(
                        text = "Apply For This Job",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

            } else if (showApplyButton) {

                Card(
                    modifier = Modifier.fillMaxWidth(),

                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    ),

                    shape = RoundedCornerShape(18.dp)
                ) {

                    Text(
                        text = "Application Sent ✅",

                        modifier = Modifier.padding(18.dp),

                        color = Color(0xFF2E7D32),

                        fontWeight = FontWeight.Bold,

                        fontSize = 15.sp
                    )
                }
            }

            // MARK AS COMPLETE BUTTON
            if (
                !showApplyButton &&
                !isCompleted &&
                fundiId == FirebaseAuth.getInstance().currentUser?.uid
            ) {

                Spacer(modifier = Modifier.height(20.dp))

                Button(

                    onClick = {

                        val database =
                            FirebaseDatabase.getInstance().reference

                        database.child("jobs")
                            .child(jobId)
                            .get()
                            .addOnSuccessListener { snapshot ->

                                val job =
                                    snapshot.getValue(JobModel::class.java)

                                if (job != null) {

                                    job.id = jobId
                                    job.status = "completed"

                                    // UPDATE MAIN JOB
                                    database.child("jobs")
                                        .child(jobId)
                                        .setValue(job)

                                    // SAVE TO FUNDI COMPLETED
                                    database.child("completedJobs")
                                        .child(fundiId)
                                        .child(jobId)
                                        .setValue(job)

                                    // SAVE TO CLIENT COMPLETED
                                    database.child("clientCompletedJobs")
                                        .child(clientId)
                                        .child(jobId)
                                        .setValue(job)

                                    // REMOVE FROM ACTIVE
                                    database.child("ongoingJobs")
                                        .child(fundiId)
                                        .child(jobId)
                                        .removeValue()

                                    database.child("clientOngoingJobs")
                                        .child(clientId)
                                        .child(jobId)
                                        .removeValue()

                                    // =========================
                                    // UPDATE FUNDI EARNINGS
                                    // =========================

                                    database.child("users")
                                        .child(fundiId)
                                        .child("earnings")
                                        .get()

                                        .addOnSuccessListener { earningsSnapshot ->

                                            val currentEarnings =
                                                earningsSnapshot.getValue(Double::class.java)
                                                    ?: 0.0

                                            val jobAmount =
                                                budget
                                                    .replace("Ksh", "")
                                                    .replace("KES", "")
                                                    .replace(",", "")
                                                    .trim()
                                                    .toDoubleOrNull() ?: 0.0

                                            val newEarnings =
                                                currentEarnings + jobAmount

                                            // UPDATE USER EARNINGS
                                            database.child("users")
                                                .child(fundiId)
                                                .child("earnings")
                                                .setValue(newEarnings)

                                            // UPDATE FUNDI STATS EARNINGS
                                            database.child("fundiStats")
                                                .child(fundiId)
                                                .child("earnings")
                                                .setValue(newEarnings)
                                        }

                                    // NOTIFY CLIENT
                                    val notificationId =
                                        database.push().key ?: ""

                                    val notificationData = mapOf(

                                        "title" to "Job Completed",

                                        "message" to
                                                "The worker marked \"$jobTitle\" as completed.",

                                        "read" to false,

                                        "timestamp" to
                                                System.currentTimeMillis()
                                    )

                                    database.child("notifications")
                                        .child(clientId)
                                        .child(notificationId)
                                        .setValue(notificationData)

                                    isCompleted = true

                                    Toast.makeText(
                                        context,
                                        "Job marked as completed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),

                    shape = RoundedCornerShape(18.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E7D32)
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Mark As Complete",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            // COMPLETED STATE
            if (isCompleted) {

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),

                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    ),

                    shape = RoundedCornerShape(18.dp)
                ) {

                    Text(
                        text = "Job Completed ✅",

                        modifier = Modifier.padding(18.dp),

                        color = Color(0xFF2E7D32),

                        fontWeight = FontWeight.Bold,

                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JobDetailsScreenPreview() {

    JobDetailsScreen(
        navController = rememberNavController(),
        jobId = "",
        showApplyButton = true
    )
}