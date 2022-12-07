package com.indev.claraa.apiResponse

data class UserRegistrationResponse(
    val status : Int,
    val message : String,
    var last_user_id : Int,
)