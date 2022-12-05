package com.indev.claraa.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.indev.claraa.entities.UserRegistrationModel
import com.indev.claraa.restApi.ClaraaApi
import com.indev.claraa.restApi.ClientApi
import com.indev.claraa.roomdb.RoomDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRegistrationRepository {

    companion object {
        private var dataBase: RoomDB? = null
        val apiInterface = ClientApi.getClient()?.create(ClaraaApi::class.java)

        private fun initializeDB(context: Context): RoomDB? {
            return RoomDB.getDatabase(context)
        }

        suspend fun insertUserData(context: Context, userRegistrationTable: UserRegistrationModel) {
            dataBase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.insertUserData(userRegistrationTable)
            }
        }

        fun getRegistrationData(context: Context): LiveData<UserRegistrationModel>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getRegistrationData()
        }

        fun updateData(context: Context, userRegistrationTable: UserRegistrationModel){
            dataBase = initializeDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.update(userRegistrationTable)
            }
        }


        suspend fun userRegistrationAPI(userRegistrationTable: UserRegistrationModel): Int {
            try {
                var result = apiInterface?.registration(userRegistrationTable)
                return if (result?.body()?.status==1){
                    result?.body()!!.last_user_id
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