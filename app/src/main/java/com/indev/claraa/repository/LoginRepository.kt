package com.indev.claraa.repository

import android.content.Context
import android.util.Log
import com.indev.claraa.apiResponse.UserProfileRespose
import com.indev.claraa.entities.LoginModel
import com.indev.claraa.entities.UserRegistrationModel
import com.indev.claraa.restApi.ClaraaApi
import com.indev.claraa.restApi.ClientApi
import com.indev.claraa.roomdb.RoomDB

class LoginRepository {

    companion object {
        private var dataBase: RoomDB? = null

        var apiInterface = ClientApi.getClient()?.create(ClaraaApi::class.java)

        private fun initializeDB(context: Context): RoomDB? {
            return RoomDB.getDatabase(context)
        }
        suspend fun login(context: Context,loginModel: LoginModel): Int {
            val userProfileArray= ArrayList<UserProfileRespose>()

            dataBase = initializeDB(context)

            try {
                var result = apiInterface?.login(loginModel)
                return if (result?.body()?.status==1){
                    dataBase?.userDao()?.deleteUserMasterTable()
                    userProfileArray.addAll(result?.body()!!.profile_data)
                    for (i in 0 until userProfileArray.size) {
                        val user_profile = UserRegistrationModel(0, userProfileArray[i].user_id,"",userProfileArray[i].user_name,userProfileArray[i].email,userProfileArray[i].mobile_number,"",  userProfileArray[i].state_id, userProfileArray[i].district_id,"","","")
                        dataBase?.userDao()?.insertUserData(user_profile)
                    }
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