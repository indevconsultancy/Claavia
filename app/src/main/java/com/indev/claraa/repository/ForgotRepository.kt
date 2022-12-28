package com.indev.claraa.repository

import android.content.Context
import android.util.Log
import com.indev.claraa.apiResponse.ForgotResponse
import com.indev.claraa.apiResponse.UserProfileRespose
import com.indev.claraa.entities.ForgotModel
import com.indev.claraa.entities.LoginModel
import com.indev.claraa.entities.UserRegistrationModel
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.restApi.ClaraaApi
import com.indev.claraa.restApi.ClientApi
import com.indev.claraa.roomdb.RoomDB

class ForgotRepository {
    companion object{
        private var dataBase: RoomDB? = null
        lateinit var prefHelper: PrefHelper

        var apiInterface = ClientApi.getClient()?.create(ClaraaApi::class.java)

        private fun initializeDB(context: Context): RoomDB? {
            return RoomDB.getDatabase(context)
        }

        suspend fun forgot(context: Context,forgotModel: ForgotModel): Int {
            dataBase = initializeDB(context)
            prefHelper = PrefHelper(context)
            try {
                var result = apiInterface?.forgetPassword(forgotModel)
                return if (result?.body()?.status==1){
                    result?.body()!!.status
                } else {
                    0
                }
            } catch (e: Exception) {
                Log.d("fail", "$e")
            }
            return 0
        }
    }
}