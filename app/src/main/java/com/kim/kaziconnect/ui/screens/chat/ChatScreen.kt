package com.kim.kaziconnect.ui.screens.messages

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kim.kaziconnect.models.MessageModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    chatId: String,
    receiverId: String,
    receiverName: String
) {

    val colorPrimary = Color(0xFF1B263B)
    val deepCobalt = Color(0xFF0D1B2A)
    val cobaltLight = Color(0xFF243B55)
    val colorAccent = Color(0xFFEE6C4D)
    val lightBg = Color(0xFFF4F7FB)

    val currentUserId =
        FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val database = FirebaseDatabase.getInstance().reference

    val focusManager = LocalFocusManager.current

    var messageText by remember {
        mutableStateOf("")
    }

    val messagesList = remember {
        mutableStateListOf<MessageModel>()
    }

    val listState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()

    /*
    MARK CHAT AS OPENED
     */
    LaunchedEffect(Unit) {

        /*
        RESET UNREAD COUNT
         */
        database.child("chats")
            .child(chatId)
            .child("unreadCount")
            .child(currentUserId)
            .setValue(0)

        database.child("messages")
            .child(chatId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    messagesList.clear()

                    for (messageSnapshot in snapshot.children) {

                        val message =
                            messageSnapshot.getValue(MessageModel::class.java)

                        if (message != null) {

                            messagesList.add(message)

                            /*
                            AUTO MARK AS SEEN
                             */
                            if (
                                message.receiverId == currentUserId &&
                                !message.seen
                            ) {

                                messageSnapshot.ref
                                    .child("seen")
                                    .setValue(true)
                            }
                        }
                    }

                    coroutineScope.launch {

                        if (messagesList.isNotEmpty()) {

                            listState.animateScrollToItem(
                                messagesList.size - 1
                            )
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

            TopAppBar(

                title = {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Surface(
                            modifier = Modifier.size(44.dp),
                            shape = CircleShape,
                            color = colorAccent.copy(alpha = 0.12f)
                        ) {

                            Box(
                                contentAlignment = Alignment.Center
                            ) {

                                Text(
                                    text = receiverName.take(1).uppercase(),
                                    color = colorAccent,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {

                            Text(
                                text = receiverName,
                                fontWeight = FontWeight.Bold,
                                color = colorPrimary,
                                fontSize = 18.sp
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF4CAF50))
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Text(
                                    text = "Online",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
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
                .pointerInput(Unit) {

                    detectTapGestures {
                        focusManager.clearFocus()
                    }
                }
        ) {

            /*
            WHATSAPP STYLE BACKGROUND
             */

            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {

                /*
                TOP LEFT GLOW
                 */
                drawCircle(
                    color = deepCobalt.copy(alpha = 0.08f),
                    radius = 420f,
                    center = Offset(
                        -120f,
                        -80f
                    )
                )

                /*
                RIGHT GLOW
                 */
                drawCircle(
                    color = cobaltLight.copy(alpha = 0.05f),
                    radius = 260f,
                    center = Offset(
                        size.width + 60f,
                        size.height / 2
                    )
                )

                /*
                BOTTOM GLOW
                 */
                drawCircle(
                    color = colorAccent.copy(alpha = 0.05f),
                    radius = 340f,
                    center = Offset(
                        size.width - 100f,
                        size.height + 100f
                    )
                )

                /*
                DOT PATTERN
                 */
                val dotColor =
                    deepCobalt.copy(alpha = 0.045f)

                for (x in 0..12) {

                    for (y in 0..26) {

                        drawCircle(
                            color = dotColor,
                            radius = 4f,

                            center = Offset(
                                x = 60f + (x * 110),
                                y = 60f + (y * 95)
                            )
                        )
                    }
                }

                /*
                FLOATING DOTS
                 */
                drawCircle(
                    color = colorAccent.copy(alpha = 0.09f),
                    radius = 10f,
                    center = Offset(
                        220f,
                        520f
                    )
                )

                drawCircle(
                    color = deepCobalt.copy(alpha = 0.07f),
                    radius = 8f,
                    center = Offset(
                        850f,
                        920f
                    )
                )

                drawCircle(
                    color = cobaltLight.copy(alpha = 0.07f),
                    radius = 6f,
                    center = Offset(
                        650f,
                        280f
                    )
                )
            }

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                if (messagesList.isEmpty()) {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),

                        contentAlignment = Alignment.Center
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "💬",
                                fontSize = 60.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Start your conversation",
                                color = colorPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Messages between clients and fundis appear here",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 40.dp)
                            )
                        }
                    }

                } else {

                    LazyColumn(

                        state = listState,

                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),

                        contentPadding = PaddingValues(
                            start = 12.dp,
                            end = 12.dp,
                            top = 16.dp,
                            bottom = 16.dp
                        ),

                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        items(messagesList) { message ->

                            val isMe =
                                message.senderId == currentUserId

                            Row(

                                modifier = Modifier.fillMaxWidth(),

                                horizontalArrangement =
                                    if (isMe)
                                        Arrangement.End
                                    else
                                        Arrangement.Start
                            ) {

                                Card(

                                    colors = CardDefaults.cardColors(
                                        containerColor =
                                            if (isMe)
                                                colorAccent
                                            else
                                                Color.White
                                    ),

                                    shape = RoundedCornerShape(
                                        topStart = 22.dp,
                                        topEnd = 22.dp,
                                        bottomStart =
                                            if (isMe) 22.dp else 6.dp,
                                        bottomEnd =
                                            if (isMe) 6.dp else 22.dp
                                    ),

                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 2.dp
                                    ),

                                    modifier = Modifier.widthIn(max = 300.dp)
                                ) {

                                    Column {

                                        Text(

                                            text = message.text,

                                            modifier = Modifier.padding(
                                                start = 16.dp,
                                                end = 16.dp,
                                                top = 13.dp,
                                                bottom = 4.dp
                                            ),

                                            color =
                                                if (isMe)
                                                    Color.White
                                                else
                                                    colorPrimary,

                                            fontSize = 15.sp,
                                            lineHeight = 22.sp
                                        )

                                        /*
                                        SEEN STATUS
                                         */
                                        if (isMe) {

                                            Text(

                                                text =
                                                    if (message.seen)
                                                        "Seen"
                                                    else
                                                        "Sent",

                                                modifier = Modifier
                                                    .align(Alignment.End)
                                                    .padding(
                                                        end = 12.dp,
                                                        bottom = 10.dp
                                                    ),

                                                color = Color.White.copy(
                                                    alpha = 0.75f
                                                ),

                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Surface(

                    color = Color.White,
                    tonalElevation = 10.dp,
                    shadowElevation = 10.dp,

                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .imePadding()
                ) {

                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 12.dp,
                                vertical = 10.dp
                            ),

                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        OutlinedTextField(

                            value = messageText,

                            onValueChange = {
                                messageText = it
                            },

                            placeholder = {
                                Text("Type a message...")
                            },

                            modifier = Modifier.weight(1f),

                            shape = RoundedCornerShape(28.dp),

                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Send
                            ),

                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorAccent,
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),

                            maxLines = 5
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        FloatingActionButton(

                            onClick = {

                                if (messageText.isBlank()) {
                                    return@FloatingActionButton
                                }

                                val messageId =
                                    database.child("messages")
                                        .child(chatId)
                                        .push()
                                        .key ?: ""

                                val message = MessageModel(

                                    messageId = messageId,
                                    senderId = currentUserId,
                                    receiverId = receiverId,
                                    text = messageText,
                                    timestamp = System.currentTimeMillis(),
                                    seen = false
                                )

                                database.child("messages")
                                    .child(chatId)
                                    .child(messageId)
                                    .setValue(message)

                                /*
                                UPDATE CHAT
                                 */
                                val chatData = mapOf(

                                    "chatId" to chatId,
                                    "lastMessage" to messageText,
                                    "lastTimestamp" to System.currentTimeMillis(),

                                    "participants/$currentUserId" to true,
                                    "participants/$receiverId" to true
                                )

                                database.child("chats")
                                    .child(chatId)
                                    .updateChildren(chatData)

                                /*
                                INCREMENT UNREAD COUNT
                                 */
                                database.child("chats")
                                    .child(chatId)
                                    .child("unreadCount")
                                    .child(receiverId)
                                    .runTransaction(object : Transaction.Handler {

                                        override fun doTransaction(
                                            currentData: MutableData
                                        ): Transaction.Result {

                                            var count =
                                                currentData.getValue(Int::class.java)
                                                    ?: 0

                                            count++

                                            currentData.value = count

                                            return Transaction.success(currentData)
                                        }

                                        override fun onComplete(
                                            error: DatabaseError?,
                                            committed: Boolean,
                                            currentData: DataSnapshot?
                                        ) {

                                        }
                                    })

                                messageText = ""
                            },

                            containerColor = colorAccent,
                            shape = CircleShape
                        ) {

                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {

    ChatScreen(
        navController = rememberNavController(),
        chatId = "sampleChat",
        receiverId = "user123",
        receiverName = "Tom"
    )
}