package com.indev.claraa.restApi

import com.google.gson.JsonObject
import com.indev.claraa.apiResponse.LoginResponse
import com.indev.claraa.apiResponse.UserRegistrationResponse
import com.indev.claraa.apiResponse.stateMasterResponse
import com.indev.claraa.entities.LoginModel
import com.indev.claraa.entities.MasterData
import com.indev.claraa.entities.UserRegistrationModel
import org.json.JSONArray
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ClaraaApi {

    @POST("registration.php")
    suspend fun registration(@Body userRegistrationModel: UserRegistrationModel):  Response<UserRegistrationResponse>


    @POST("download_general.php")
    suspend fun downloadMasterData(@Body masterData: MasterData): Response<List<stateMasterResponse>>


    @POST("login.php")
    suspend fun login(@Body loginModel: LoginModel):  Response<LoginResponse>

}