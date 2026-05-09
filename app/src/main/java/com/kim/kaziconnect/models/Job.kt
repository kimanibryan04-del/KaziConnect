package com.kim.kaziconnect.models

data class JobModel(

    var id: String = "",
    var title: String = "",
    var description: String = "",
    var location: String = "",
    var budget: String = "",
    var clientId: String = "",
    var status: String = "open",
    var category: String = "",
    var fundiName: String = "",
    var clientName: String = "",


    // ASSIGNED WORKER
    var fundiId: String = "",

    // COMPLETION + REVIEW TRACKING
    var fundiCompleted: Boolean = false,
    var clientReviewed: Boolean = false,
    var fundiReviewed: Boolean = false
)