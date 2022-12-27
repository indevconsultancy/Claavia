package com.indev.claraa.restApi

import com.google.gson.JsonArray
import com.indev.claraa.apiResponse.DeleteAPIResponse
import com.indev.claraa.apiResponse.InsertAPIResponse
import com.indev.claraa.apiResponse.LoginResponse
import com.indev.claraa.apiResponse.UpdateAPIResponse
import com.indev.claraa.entities.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface ClaraaApi {

    @POST("registration.php")
    suspend fun registration(@Body userRegistrationModel: UserRegistrationModel):  Response<InsertAPIResponse>

    @POST("profile_update.php")
    suspend fun updateProfile(@Body userRegistrationModel: UserRegistrationModel,@Header("Authorization") authorization:Any):  Response<UpdateAPIResponse>

    @POST("download_general.php")
    suspend fun downloadMasterData(@Body masterData: MasterData): Response<JsonArray>

    @POST("login.php")
    suspend fun login(@Body loginModel: LoginModel):  Response<LoginResponse>

    @POST("address_insert.php")
    suspend fun addressDetails(@Body addressDetailsModel: AddressDetailsModel,@Header("Authorization") authorization:Any): Response<InsertAPIResponse>

    @POST("address_update.php")
    suspend fun updateAddress(@Body addressDetailsModel: AddressDetailsModel,@Header("Authorization") authorization:Any):  Response<UpdateAPIResponse>

    @POST("address_delete.php")
    suspend fun deleteAddress(@Body id: String,@Header("Authorization") authorization:Any):  Response<DeleteAPIResponse>

    @POST("cart_insert.php")
    suspend fun cartInsertAPI(@Body cartModel: CartModel,@Header("Authorization") authorization:Any): Response<InsertAPIResponse>

    @POST("cart_update.php")
    suspend fun cartUpdateAPI(@Body cartModel: CartModel,@Header("Authorization") authorization:Any):  Response<UpdateAPIResponse>

    @POST("cart_delete.php")
    suspend fun deleteCartAPI(@Body id: String,@Header("Authorization") authorization:Any):  Response<DeleteAPIResponse>

    @POST("ordermaster_insert.php")
    suspend fun insertOrderMasterAPI(@Body orderMasterModel: OrderMasterModel,@Header("Authorization") authorization:Any): Response<InsertAPIResponse>

    @POST("orderdetails_insert.php")
    suspend fun insertOrderDetailAPI(@Body orderDetailsModel: OrderDetailsModel,@Header("Authorization") authorization:Any): Response<InsertAPIResponse>

}