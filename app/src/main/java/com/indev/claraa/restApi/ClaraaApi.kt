package com.indev.claraa.restApi

import com.google.gson.JsonArray
import com.indev.claraa.apiResponse.AddressDetailsResponse
import com.indev.claraa.apiResponse.LoginResponse
import com.indev.claraa.apiResponse.UserRegistrationResponse
import com.indev.claraa.entities.AddressDetailsModel
import com.indev.claraa.entities.LoginModel
import com.indev.claraa.entities.MasterData
import com.indev.claraa.entities.UserRegistrationModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface ClaraaApi {

    @POST("registration.php")
    suspend fun registration(@Body userRegistrationModel: UserRegistrationModel):  Response<UserRegistrationResponse>

    @POST("profile_update.php")
    suspend fun updateProfile(@Body userRegistrationModel: UserRegistrationModel,@Header("Authorization") authorization:Any):  Response<UserRegistrationResponse>

    @POST("download_general.php")
    suspend fun downloadMasterData(@Body masterData: MasterData): Response<JsonArray>

    @POST("login.php")
    suspend fun login(@Body loginModel: LoginModel):  Response<LoginResponse>

    @POST("address_insert.php")
    suspend fun addressDetails(@Body addressDetailsModel: AddressDetailsModel,@Header("Authorization") authorization:Any): Response<AddressDetailsResponse>

    @POST("address_update.php")
    suspend fun updateAddress(@Body addressDetailsModel: AddressDetailsModel,@Header("Authorization") authorization:Any):  Response<AddressDetailsResponse>

}