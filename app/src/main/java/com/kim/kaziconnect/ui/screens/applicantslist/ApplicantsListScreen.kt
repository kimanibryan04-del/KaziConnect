package com.kim.kaziconnect.ui.screens.applicants

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.*
import com.kim.kaziconnect.models.ApplicantModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicantListScreen(
    navController: NavHostController,
    jobId: String
) {

    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    val applicants = remember {
        mutableStateListOf<ApplicantModel>()
    }

    LaunchedEffect(Unit) {

        FirebaseDatabase.getInstance().reference
            .child("applications")
            .child(jobId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    applicants.clear()

                    for (applicantSnapshot in snapshot.children) {

                        val userId = applicantSnapshot.key ?: ""

                        FirebaseDatabase.getInstance().reference
                            .child("users")
                            .child(userId)
                            .get()
                            .addOnSuccessListener { userData ->

                                val applicant =
                                    userData.getValue(ApplicantModel::class.java)

                                if (applicant != null) {

                                    applicant.userId = userId

                                    applicants.add(applicant)
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
                        text = "Applicants",
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
                            contentDescription = "Back",
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(lightBg)
        ) {

            if (applicants.isEmpty()) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "No applicants yet",
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

                    items(applicants) { applicant ->

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            )
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text(
                                    text = applicant.fullName,
                                    fontWeight = FontWeight.Bold,
                                    color = colorPrimary,
                                    fontSize = 18.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "Skill: ${applicant.skill}",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Phone: ${applicant.phone}",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Email: ${applicant.email}",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "⭐ ${applicant.rating}",
                                    color = colorAccent,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ApplicantListScreenPreview() {
    ApplicantListScreen(
        navController = rememberNavController(),
        jobId = ""
    )
}