package com.kim.kaziconnect.models

data class NotificationData(

    val title: String = "",
    val message: String = "",

    // CHANGE THIS
    val timestamp: Long = 0L,

    val read: Boolean = false,

    // OPTIONAL EXTRA DATA
    val type: String = "",

    val jobId: String = "",
    val senderId: String = ""
)