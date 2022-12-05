package com.indev.claraa.apiResponse

data class UserRegistration(
    val status : Int,
    val message : String,
    var last_user_id : Int,
)