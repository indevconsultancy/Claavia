package com.indev.claraa.apiResponse

data class UserProfileRespose(val user_name: String,
                              val password: String,
                              val user_id: Int,
                              val mobile_number: String,
                              val email: String,
                              val state_id: String,
                              val district_id: String)
