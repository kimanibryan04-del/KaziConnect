package com.kim.kaziconnect.models

data class ReviewModel(

    var reviewId: String = "",
    var jobId: String = "",

    var reviewerId: String = "",
    var receiverId: String = "",

    var rating: Float = 0f,
    var comment: String = "",

    var timestamp: Long = 0L
)