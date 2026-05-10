package com.kim.kaziconnect.ui.screens.publicprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.*
import com.kim.kaziconnect.models.ReviewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientPublicProfileScreen(
    navController: NavController,
    clientId: String
) {

    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var profileImage by remember {
        mutableStateOf("")
    }

    var rating by remember { mutableStateOf(0.0) }
    var jobsPosted by remember { mutableStateOf(0) }

    var reviews by remember {
        mutableStateOf(listOf<Pair<ReviewModel, String>>())
    }

    LaunchedEffect(clientId) {

        val database =
            FirebaseDatabase.getInstance().reference

        /*
        LOAD CLIENT DATA
         */
        database.child("users")
            .child(clientId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    name =
                        snapshot.child("name")
                            .getValue(String::class.java)
                            ?: "Unknown Client"

                    location =
                        snapshot.child("location")
                            .getValue(String::class.java)
                            ?: "Unknown Location"

                    phone =
                        snapshot.child("phone")
                            .getValue(String::class.java)
                            ?: "No Phone"

                    profileImage =
                        snapshot.child("profileImage")
                            .getValue(String::class.java)
                            ?: ""

                    rating =
                        snapshot.child("rating")
                            .getValue(Float::class.java)
                            ?.toDouble()
                            ?: 0.0
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        /*
        LOAD JOBS POSTED COUNT
         */
        database.child("jobs")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    var count = 0

                    for (jobSnapshot in snapshot.children) {

                        val ownerId =
                            jobSnapshot.child("clientId")
                                .getValue(String::class.java)
                                ?: ""

                        if (ownerId == clientId) {

                            count++
                        }
                    }

                    jobsPosted = count
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        /*
        LOAD REVIEWS
         */
        database.child("reviews")
            .child(clientId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val reviewList =
                        mutableListOf<Pair<ReviewModel, String>>()

                    for (reviewSnapshot in snapshot.children) {

                        val review =
                            reviewSnapshot.getValue(ReviewModel::class.java)

                        if (review != null) {

                            val reviewerName =
                                if (review.reviewerName.isNotBlank())
                                    review.reviewerName
                                else
                                    "Anonymous"

                            reviewList.add(
                                Pair(review, reviewerName)
                            )

                            reviews =
                                reviewList.sortedByDescending {
                                    it.first.timestamp
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

            TopAppBar(

                title = {

                    Text(
                        text = "Client Profile",
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
                            contentDescription = null,
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

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .background(lightBg)
                .padding(paddingValues),

            contentPadding = PaddingValues(20.dp),

            verticalArrangement = Arrangement.spacedBy(14.dp)

        ) {

            item {

                Card(

                    modifier = Modifier.fillMaxWidth(),

                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),

                    shape = RoundedCornerShape(24.dp)
                ) {

                    Column(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),

                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(

                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(colorAccent.copy(alpha = 0.15f)),

                            contentAlignment = Alignment.Center
                        ) {

                            if (
                                profileImage.isNotBlank() &&
                                profileImage != "null"
                            ) {

                                AsyncImage(
                                    model = profileImage,
                                    contentDescription = null,

                                    modifier = Modifier.fillMaxSize()
                                )

                            } else {

                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = colorAccent,
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = colorPrimary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Client",
                            color = colorAccent,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            item {

                Row(

                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    ProfileStatCard(
                        title = "Rating",
                        value = String.format("%.1f", rating),
                        icon = Icons.Default.Star,
                        iconColor = Color(0xFFFFD700),
                        modifier = Modifier.weight(1f)
                    )

                    ProfileStatCard(
                        title = "Jobs Posted",
                        value = jobsPosted.toString(),
                        icon = Icons.Default.Work,
                        iconColor = colorAccent,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {

                Text(
                    text = "Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = colorPrimary
                )
            }

            item {

                DetailCard(
                    icon = Icons.Default.LocationOn,
                    label = "Location",
                    value = location,
                    colorAccent = colorAccent
                )
            }

            item {

                DetailCard(
                    icon = Icons.Default.Person,
                    label = "Phone",
                    value = phone,
                    colorAccent = colorAccent
                )
            }

            item {

                Text(
                    text = "Reviews",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = colorPrimary
                )
            }

            if (reviews.isEmpty()) {

                item {

                    Card(

                        modifier = Modifier.fillMaxWidth(),

                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),

                        shape = RoundedCornerShape(18.dp)
                    ) {

                        Text(
                            text = "No reviews yet",

                            modifier = Modifier.padding(20.dp),

                            color = Color.Gray,

                            fontSize = 15.sp
                        )
                    }
                }

            } else {

                items(reviews) { pair ->

                    val review = pair.first
                    val reviewerName = pair.second

                    Card(

                        modifier = Modifier.fillMaxWidth(),

                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),

                        shape = RoundedCornerShape(20.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(18.dp)
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                repeat(review.rating.toInt()) {

                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFFFB300),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = String.format("%.1f", review.rating),
                                    fontWeight = FontWeight.Bold,
                                    color = colorPrimary
                                )
                            }

                            if (review.comment.isNotBlank()) {

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = review.comment,
                                    color = Color.DarkGray,
                                    fontSize = 14.sp,
                                    lineHeight = 22.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = reviewerName,
                                color = Color.Gray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            item {

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClientPublicProfileScreenPreview() {

    ClientPublicProfileScreen(
        navController = rememberNavController(),
        clientId = "sampleId"
    )
}