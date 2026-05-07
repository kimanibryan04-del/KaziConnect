package com.kim.kaziconnect.models

data class JobModel(

    var id: String = "",
    var title: String = "",
    var description: String = "",
    var location: String = "",
    var budget: String = "",
    var clientId: String = "",
    var status: String = "open"
)