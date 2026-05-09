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
import androidx.compose.material.icons.filled.Payments
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundiPublicProfileScreen(
    navController: NavController,
    fundiId: String
) {

    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    var name by remember { mutableStateOf("") }
    var skill by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var rating by remember { mutableStateOf(0.0) }
    var earnings by remember { mutableStateOf(0.0) }
    var completedJobs by remember { mutableStateOf(0) }

    var reviews by remember {
        mutableStateOf(listOf<ReviewModel>())
    }

    LaunchedEffect(fundiId) {

        val database = FirebaseDatabase.getInstance().reference

        /*
        ALL USER DATA
         */
        database.child("users")
            .child(fundiId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    name =
                        snapshot.child("name")
                            .getValue(String::class.java)
                            ?: "Unknown Fundi"

                    skill =
                        snapshot.child("skill")
                            .getValue(String::class.java)
                            ?: "No Skill"

                    location =
                        snapshot.child("location")
                            .getValue(String::class.java)
                            ?: "Unknown Location"

                    phone =
                        snapshot.child("phone")
                            .getValue(String::class.java)
                            ?: "No Phone"

                    rating =
                        snapshot.child("rating")
                            .getValue(Float::class.java)
                            ?.toDouble()
                            ?: 0.0

                    earnings =
                        snapshot.child("earnings")
                            .getValue(Double::class.java)
                            ?: 0.0

                    completedJobs =
                        snapshot.child("completedJobs")
                            .getValue(Int::class.java)
                            ?: 0
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        /*
        LOAD REVIEWS
         */
        database.child("reviews")
            .child(fundiId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val reviewsList = mutableListOf<ReviewModel>()

                    for (reviewSnapshot in snapshot.children) {

                        val review =
                            reviewSnapshot.getValue(ReviewModel::class.java)

                        if (review != null) {

                            reviewsList.add(review)
                        }
                    }

                    reviews = reviewsList.sortedByDescending {
                        it.timestamp
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
                        text = "Fundi Profile",
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

                Spacer(modifier = Modifier.height(10.dp))
            }

            /*
            PROFILE HEADER
             */
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

                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = colorAccent,
                                modifier = Modifier.size(50.dp)
                            )
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
                            text = skill,
                            color = colorAccent,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            /*
            STATS
             */
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
                        title = "Jobs",
                        value = completedJobs.toString(),
                        icon = Icons.Default.Work,
                        iconColor = colorAccent,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {

                ProfileStatCard(
                    title = "Earnings",
                    value = "KES ${String.format("%.0f", earnings)}",
                    icon = Icons.Default.Payments,
                    iconColor = Color(0xFF4CAF50),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            /*
            DETAILS TITLE
             */
            item {

                Text(
                    text = "Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = colorPrimary
                )
            }

            /*
            LOCATION
             */
            item {

                DetailCard(
                    icon = Icons.Default.LocationOn,
                    label = "Location",
                    value = location,
                    colorAccent = colorAccent
                )
            }

            /*
            PHONE
             */
            item {

                DetailCard(
                    icon = Icons.Default.Person,
                    label = "Phone",
                    value = phone,
                    colorAccent = colorAccent
                )
            }

            /*
            REVIEWS TITLE
             */
            item {

                Text(
                    text = "Reviews",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = colorPrimary
                )
            }

            /*
            EMPTY REVIEWS
             */
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

                items(reviews) { review ->

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
                                    text = "${review.rating}/5",
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
                                text = "Anonymous Client",
                                color = Color.Gray,
                                fontSize = 12.sp
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

@Composable
fun ProfileStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    modifier: Modifier
) {

    Card(

        modifier = modifier,

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        shape = RoundedCornerShape(20.dp)
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(26.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                color = Color.Gray,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                color = Color(0xFF1B263B),
                fontWeight = FontWeight.Black,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun DetailCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    colorAccent: Color
) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        shape = RoundedCornerShape(18.dp)
    ) {

        Row(

            modifier = Modifier.padding(18.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                shape = CircleShape,
                color = colorAccent.copy(alpha = 0.12f)
            ) {

                Box(
                    modifier = Modifier.size(46.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = colorAccent
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {

                Text(
                    text = label,
                    color = Color.Gray,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = value,
                    color = Color(0xFF1B263B),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FundiPublicProfileScreenPreview() {

    FundiPublicProfileScreen(
        navController = rememberNavController(),
        fundiId = "sampleId"
    )
}