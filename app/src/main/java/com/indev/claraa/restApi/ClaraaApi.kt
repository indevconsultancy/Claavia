package com.indev.claraa.restApi

import com.indev.claraa.apiResponse.LoginResponse
import com.indev.claraa.apiResponse.ProductMasterResponse
import com.indev.claraa.apiResponse.UserRegistrationResponse
import com.indev.claraa.entities.LoginModel
import com.indev.claraa.entities.ProductMasterModel
import com.indev.claraa.entities.UserRegistrationModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ClaraaApi {

    @POST("registration.php")
    suspend fun registration(@Body userRegistrationModel: UserRegistrationModel):  Response<UserRegistrationResponse>


    @POST("download_product_master.php")
    suspend fun productMaster(@Body productMasterModel: ProductMasterModel):  Response<ProductMasterResponse>

    @POST("login.php")
    suspend fun login(@Body loginModel: LoginModel):  Response<LoginResponse>

}