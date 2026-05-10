package com.kim.kaziconnect.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodsScreen(navController: NavController) {

    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF1F4F9)

    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    var selectedMethod by remember {
        mutableStateOf("M-Pesa")
    }

    var phoneNumber by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {

        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(userId)
            .child("paymentInfo")
            .get()
            .addOnSuccessListener { snapshot ->

                selectedMethod =
                    snapshot.child("method")
                        .getValue(String::class.java)
                        ?: "M-Pesa"

                phoneNumber =
                    snapshot.child("phoneNumber")
                        .getValue(String::class.java)
                        ?: ""
            }
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Payment Methods",
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

        Column(

            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .background(lightBg)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Choose Payment Method",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = colorPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Set up your preferred payment method",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    PaymentOptionCard(
                        title = "M-Pesa",
                        subtitle = "Recommended for Kenyan users",
                        selected = selectedMethod == "M-Pesa",
                        onClick = {
                            selectedMethod = "M-Pesa"
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    PaymentOptionCard(
                        title = "Airtel Money",
                        subtitle = "Alternative mobile payment option",
                        selected = selectedMethod == "Airtel Money",
                        onClick = {
                            selectedMethod = "Airtel Money"
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it
                },

                label = {
                    Text("Phone Number")
                },

                leadingIcon = {

                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = colorAccent
                    )
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(18.dp),

                singleLine = true,

                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorAccent,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF3E0)
                )
            ) {

                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {

                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = colorAccent,
                        modifier = Modifier.size(22.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {

                        Text(
                            text = "Manual Payments For Now",
                            fontWeight = FontWeight.Bold,
                            color = colorPrimary,
                            fontSize = 15.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Payments are currently completed manually until in-app payments are available.",
                            color = Color.DarkGray,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(

                onClick = {

                    isLoading = true

                    val paymentData = mapOf(

                        "method" to selectedMethod,
                        "phoneNumber" to phoneNumber
                    )

                    FirebaseDatabase.getInstance().reference
                        .child("users")
                        .child(userId)
                        .child("paymentInfo")
                        .setValue(paymentData)
                        .addOnSuccessListener {

                            isLoading = false
                            navController.popBackStack()
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

                if (isLoading) {

                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )

                } else {

                    Text(
                        text = "Save Payment Method",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun PaymentOptionCard(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    val colorPrimary = Color(0xFF1B263B)
    val colorAccent = Color(0xFFEE6C4D)

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(
            containerColor =
                if (selected)
                    colorAccent.copy(alpha = 0.12f)
                else
                    Color(0xFFF8F9FB)
        )
    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        colorAccent.copy(alpha = 0.15f),
                        CircleShape
                    ),

                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = colorAccent
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = colorPrimary,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = subtitle,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            RadioButton(
                selected = selected,
                onClick = {
                    onClick()
                },

                colors = RadioButtonDefaults.colors(
                    selectedColor = colorAccent
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentMethodsScreenPreview() {

    PaymentMethodsScreen(
        rememberNavController()
    )
}