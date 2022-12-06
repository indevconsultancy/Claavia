package com.indev.claraa.apiResponse

data class LoginResponse( val message: String,
                          var status: Int,
                          val user_name: String,
                          val password: String,
                          val user_id: Int,
                          val mobile_number: String,
                          val email: String,
                          val state_name: String,
                          val district_name: String,
                          val Token: String)
