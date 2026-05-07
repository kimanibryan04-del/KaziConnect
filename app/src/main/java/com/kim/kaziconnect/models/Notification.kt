package com.kim.kaziconnect.models

data class NotificationData(
    val title: String = "",
    val message: String = "",
    val time: String = "",
    val read: Boolean = false
)