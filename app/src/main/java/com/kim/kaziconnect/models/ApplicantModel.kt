package com.kim.kaziconnect.models


data class ApplicantModel(
    var userId: String = "",
    var fullName: String = "",
    var phone: String = "",
    var skill: String = "",
    var rating: Double = 0.0
)
