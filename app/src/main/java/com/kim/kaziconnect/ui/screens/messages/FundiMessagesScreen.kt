package com.kim.kaziconnect.ui.screens.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kim.kaziconnect.models.ChatModel
import com.kim.kaziconnect.navigation.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundiMessagesScreen(navController: NavHostController) {

    val colorPrimary = Color(0xFF1B263B)
    val deepCobalt = Color(0xFF0D1B2A)
    val cobaltLight = Color(0xFF243B55)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF4F7FB)

    val currentUserId =
        FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val chatsList = remember {
        mutableStateListOf<Pair<ChatModel, String>>()
    }

    /*
    MESSAGE BADGE
     */

    var hasUnreadMessages by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {

        FirebaseDatabase.getInstance().reference
            .child("chats")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    chatsList.clear()

                    /*
                    SHOW DOT ONLY IF THERE ARE CHATS
                     */
                    hasUnreadMessages = false

                    for (chatSnapshot in snapshot.children) {

                        val chat =
                            chatSnapshot.getValue(ChatModel::class.java)

                        if (
                            chat != null &&
                            chat.participants.containsKey(currentUserId)
                        ) {

                            hasUnreadMessages = true

                            val otherUserId =
                                chat.participants.keys.firstOrNull {
                                    it != currentUserId
                                } ?: ""

                            FirebaseDatabase.getInstance().reference
                                .child("users")
                                .child(otherUserId)
                                .get()
                                .addOnSuccessListener { userSnapshot ->

                                    val otherUserName =
                                        userSnapshot.child("name")
                                            .getValue(String::class.java)
                                            ?: "User"

                                    chatsList.add(
                                        Pair(chat, otherUserName)
                                    )
                                }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    Scaffold(

        containerColor = lightBg,

        topBar = {

            CenterAlignedTopAppBar(

                title = {

                    Text(
                        text = "Messages",
                        fontWeight = FontWeight.Black,
                        color = colorPrimary
                    )
                },

                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },

        bottomBar = {

            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {

                /*
                HOME
                 */

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

                /*
                JOBS
                 */

                NavigationBarItem(

                    icon = {
                        Icon(
                            Icons.Outlined.List,
                            contentDescription = "Jobs"
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
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        selectedTextColor = colorAccent,
                        indicatorColor = Color.Transparent
                    )
                )

                /*
                PROFILE
                 */

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
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorAccent,
                        selectedTextColor = colorAccent,
                        indicatorColor = Color.Transparent
                    )
                )

                /*
                MESSAGES
                 */

                NavigationBarItem(

                    icon = {

                        BadgedBox(

                            badge = {

                                /*
                                DOT DISAPPEARS WHEN SCREEN IS OPENED
                                 */

                                if (hasUnreadMessages.not()) {

                                    Badge(
                                        containerColor = Color.Transparent
                                    )
                                }
                            }
                        ) {

                            Icon(
                                Icons.Filled.Email,
                                contentDescription = "Messages"
                            )
                        }
                    },

                    label = {
                        Text("Messages")
                    },

                    selected = true,

                    onClick = {

                        /*
                        REMOVE DOT WHEN OPENED
                         */

                        hasUnreadMessages = false

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

        Box(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(lightBg)
        ) {

            /*
            BACKGROUND DECOR
             */

            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .offset(x = (-120).dp, y = (-80).dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    deepCobalt.copy(alpha = 0.10f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )

                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .align(Alignment.CenterEnd)
                        .offset(x = 100.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    cobaltLight.copy(alpha = 0.07f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )

                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .align(Alignment.BottomStart)
                        .offset(x = (-90).dp, y = 90.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    colorAccent.copy(alpha = 0.06f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )
            }

            /*
            EMPTY STATE
             */

            if (chatsList.isEmpty()) {

                Column(

                    modifier = Modifier.fillMaxSize(),

                    verticalArrangement = Arrangement.Center,

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.size(70.dp),
                        tint = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "No conversations yet",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }

            } else {

                /*
                CHAT LIST
                 */

                LazyColumn(

                    modifier = Modifier.fillMaxSize(),

                    contentPadding = PaddingValues(16.dp),

                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    items(chatsList) { (chat, otherUserName) ->

                        val otherUserId =
                            chat.participants.keys.firstOrNull {
                                it != currentUserId
                            } ?: ""

                        Card(

                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {

                                    /*
                                    REMOVE DOT WHEN CHAT OPENS
                                     */

                                    hasUnreadMessages = false

                                    navController.navigate(
                                        "$ROUT_CHAT/${chat.chatId}/$otherUserId/$otherUserName"
                                    )
                                },

                            shape = RoundedCornerShape(22.dp),

                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 3.dp
                            ),

                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {

                            Row(

                                modifier = Modifier.padding(16.dp),

                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                /*
                                AVATAR
                                 */

                                Box(

                                    modifier = Modifier
                                        .size(58.dp)
                                        .clip(CircleShape)
                                        .background(
                                            brush = Brush.linearGradient(
                                                listOf(
                                                    colorAccent,
                                                    Color(0xFFFF9A7A)
                                                )
                                            )
                                        ),

                                    contentAlignment = Alignment.Center
                                ) {

                                    Text(
                                        text = otherUserName
                                            .take(1)
                                            .uppercase(),

                                        color = Color.White,

                                        fontWeight = FontWeight.Bold,

                                        fontSize = 22.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                /*
                                CHAT DETAILS
                                 */

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = otherUserName,
                                        fontWeight = FontWeight.Bold,
                                        color = colorPrimary,
                                        fontSize = 16.sp
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = chat.lastMessage,
                                        color = Color.Gray,
                                        fontSize = 14.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                /*
                                SMALL MESSAGE DOT
                                 */

                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(colorAccent)
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
fun FundiMessagesScreenPreview() {

    FundiMessagesScreen(
        navController = rememberNavController()
    )
}