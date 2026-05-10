package com.kim.kaziconnect.ui.screens.review

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
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
import com.kim.kaziconnect.models.NotificationData
import com.kim.kaziconnect.models.ReviewModel
import com.kim.kaziconnect.models.User
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.imePadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    navController: NavHostController,
    jobId: String,
    receiverId: String,
    reviewType: String
) {

    val context = LocalContext.current

    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    val currentUserId =
        FirebaseAuth.getInstance().currentUser?.uid ?: ""

    var rating by remember {
        mutableFloatStateOf(0f)
    }

    var comment by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var receiverName by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {

        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(receiverId)
            .get()
            .addOnSuccessListener {

                val user =
                    it.getValue(User::class.java)

                receiverName =
                    user?.name ?: "User"
            }
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Leave Review",
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = colorPrimary
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .background(lightBg)
                .padding(20.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "How was your experience with",
                color = Color.Gray,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = receiverName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = colorPrimary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Tap to rate",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        for (i in 1..5) {

                            Icon(

                                imageVector =
                                    if (i <= rating)
                                        Icons.Filled.Star
                                    else
                                        Icons.Outlined.StarOutline,

                                contentDescription = null,

                                tint =
                                    if (i <= rating)
                                        Color(0xFFFFB300)
                                    else
                                        Color.LightGray,

                                modifier = Modifier
                                    .size(42.dp)
                                    .clickable {

                                        rating = i.toFloat()
                                    }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    OutlinedTextField(

                        value = comment,

                        onValueChange = {
                            comment = it
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),

                        placeholder = {
                            Text("Write your review...")
                        },

                        shape = RoundedCornerShape(18.dp),

                        colors = OutlinedTextFieldDefaults.colors(

                            focusedBorderColor = colorAccent,

                            unfocusedBorderColor = Color.LightGray
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(

                onClick = {

                    if (rating == 0f) {

                        Toast.makeText(
                            context,
                            "Please select a rating",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

                    isLoading = true

                    val database =
                        FirebaseDatabase.getInstance().reference

                    val reviewId =
                        database.child("reviews")
                            .push()
                            .key ?: ""

                    val review = ReviewModel(

                        reviewId = reviewId,

                        jobId = jobId,

                        reviewerId = currentUserId,

                        receiverId = receiverId,

                        rating = rating,

                        comment = comment,

                        timestamp = System.currentTimeMillis()
                    )

                    // SAVE REVIEW
                    database.child("reviews")
                        .child(receiverId)
                        .child(reviewId)
                        .setValue(review)

                        .addOnSuccessListener {

                            // UPDATE USER RATING
                            database.child("users")
                                .child(receiverId)
                                .get()

                                .addOnSuccessListener { snapshot ->

                                    val user =
                                        snapshot.getValue(User::class.java)

                                    var newRating = rating

                                    if (user != null) {

                                        val currentRating =
                                            user.rating

                                        val currentReviewCount =
                                            user.reviewCount

                                        val newReviewCount =
                                            currentReviewCount + 1

                                        newRating =

                                            (
                                                    (currentRating * currentReviewCount)
                                                            + rating
                                                    ) / newReviewCount

                                        database.child("users")
                                            .child(receiverId)
                                            .child("rating")
                                            .setValue(newRating)

                                        database.child("users")
                                            .child(receiverId)
                                            .child("reviewCount")
                                            .setValue(newReviewCount)

                                        database.child("users")
                                            .child(receiverId)
                                            .child("completedJobs")
                                            .setValue(newReviewCount)
                                    }

                                    // CLIENT REVIEWED FUNDI
                                    if (reviewType == "client_to_fundi") {

                                        database.child("jobs")
                                            .child(jobId)
                                            .child("clientReviewed")
                                            .setValue(true)

                                    } else {

                                        // FUNDI REVIEWED CLIENT
                                        database.child("jobs")
                                            .child(jobId)
                                            .child("fundiReviewed")
                                            .setValue(true)
                                    }

                                    // UPDATE COMPLETED JOB COPIES
                                    database.child("jobs")
                                        .child(jobId)
                                        .get()

                                        .addOnSuccessListener { jobSnapshot ->

                                            val fundiId =
                                                jobSnapshot.child("fundiId")
                                                    .getValue(String::class.java) ?: ""

                                            val clientId =
                                                jobSnapshot.child("clientId")
                                                    .getValue(String::class.java) ?: ""

                                            if (reviewType == "client_to_fundi") {

                                                // UPDATE FUNDI SIDE
                                                database.child("completedJobs")
                                                    .child(fundiId)
                                                    .child(jobId)
                                                    .child("clientReviewed")
                                                    .setValue(true)

                                                // UPDATE CLIENT SIDE
                                                database.child("clientCompletedJobs")
                                                    .child(clientId)
                                                    .child(jobId)
                                                    .child("clientReviewed")
                                                    .setValue(true)

                                            } else {

                                                // UPDATE FUNDI SIDE
                                                database.child("completedJobs")
                                                    .child(fundiId)
                                                    .child(jobId)
                                                    .child("fundiReviewed")
                                                    .setValue(true)

                                                // UPDATE CLIENT SIDE
                                                database.child("clientCompletedJobs")
                                                    .child(clientId)
                                                    .child(jobId)
                                                    .child("fundiReviewed")
                                                    .setValue(true)
                                            }
                                        }

                                    // SEND NOTIFICATION
                                    val notificationId =
                                        database.push().key ?: ""

                                    val notification =
                                        NotificationData(

                                            title = "New Review",

                                            message =
                                                "You received a ${rating.toInt()} star review.",

                                            timestamp =
                                                System.currentTimeMillis(),

                                            read = false,

                                            type = "review",

                                            jobId = jobId,

                                            senderId = currentUserId
                                        )

                                    database.child("notifications")
                                        .child(receiverId)
                                        .child(notificationId)
                                        .setValue(notification)

                                    isLoading = false

                                    Toast.makeText(
                                        context,
                                        "Review submitted",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    navController.popBackStack()
                                }
                        }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),

                shape = RoundedCornerShape(18.dp),

                enabled = !isLoading,

                colors = ButtonDefaults.buttonColors(
                    containerColor = colorAccent
                )
            ) {

                if (isLoading) {

                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp
                    )

                } else {

                    Text(
                        text = "Submit Review",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewScreenPreview() {

    ReviewScreen(
        navController = rememberNavController(),
        jobId = "",
        receiverId = "",
        reviewType = "client_to_fundi"
    )
}