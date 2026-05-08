package com.kim.kaziconnect.models

data class ChatModel(

    val chatId: String = "",

    val lastMessage: String = "",

    val lastTimestamp: Long = 0L,

    val participants: Map<String, Boolean> = emptyMap(),

    /*
    ADD THIS
     */
    val unreadCount: Map<String, Int> = emptyMap()
)