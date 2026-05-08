package com.kim.kaziconnect.models

data class User(

    var uid: String = "",
    var name: String = "",
    var email: String = "",
    var role: String = "",

    var isVerified: Boolean = false,

    // NEW
    var phone: String = "",
    var skill: String = "",

    var rating: Float = 0f,
    var reviewCount: Int = 0,
    var earnings: Double = 0.0
)