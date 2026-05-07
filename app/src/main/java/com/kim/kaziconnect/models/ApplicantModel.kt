package com.kim.kaziconnect.models

data class ApplicantModel(

    var userId: String = "",
    var fullName: String = "",
    var skill: String = "",
    var phone: String = "",
    var email: String = "",
    var rating: Double = 0.0
)