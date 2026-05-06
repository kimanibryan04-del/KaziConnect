package com.kim.kaziconnect.models

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "", // "client" or "fundi"
    val isVerified: Boolean = false
)