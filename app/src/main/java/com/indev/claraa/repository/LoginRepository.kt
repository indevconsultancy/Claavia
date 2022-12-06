package com.indev.claraa.repository

import android.util.Log
import com.indev.claraa.entities.LoginModel
import com.indev.claraa.repository.UserRegistrationRepository.Companion.apiInterface
import com.indev.claraa.restApi.ClaraaApi
import com.indev.claraa.restApi.ClientApi
import com.indev.claraa.roomdb.RoomDB
import com.indev.claraa.ui.LoginScreen

class LoginRepository {

    companion object {
        var apiInterface = ClientApi.getClient()?.create(ClaraaApi::class.java)

        suspend fun login(loginModel: LoginModel): Int {
            try {
                var result = apiInterface?.login(loginModel)
                return if (result?.body()?.status==1){
                    result?.body()!!.user_id
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