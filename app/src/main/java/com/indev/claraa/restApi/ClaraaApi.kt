package com.indev.claraa.restApi

import com.indev.claraa.apiResponse.LoginResponse
import com.indev.claraa.apiResponse.UserRegistration
import com.indev.claraa.entities.LoginModel
import com.indev.claraa.entities.UserRegistrationModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ClaraaApi {

    @POST("registration.php")
    suspend fun registration(@Body userRegistrationModel: UserRegistrationModel):  Response<UserRegistration>

    @POST("login.php")
    suspend fun login(@Body loginModel: LoginModel):  Response<LoginResponse>

}