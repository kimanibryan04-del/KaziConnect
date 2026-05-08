package com.kim.kaziconnect.models

data class MessageModel(

    val messageId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val timestamp: Long = 0L,
    val seen: Boolean = false
)