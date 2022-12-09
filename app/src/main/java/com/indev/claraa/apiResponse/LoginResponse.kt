package com.indev.claraa.apiResponse

data class LoginResponse( val message: String,
                          var status: Int,
                          val profile_data: List<UserProfileRespose>,
                          val Token: String)

