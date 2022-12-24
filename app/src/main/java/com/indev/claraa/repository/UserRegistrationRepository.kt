package com.indev.claraa.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.indev.claraa.entities.DistrictModel
import com.indev.claraa.entities.StateModel
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

        fun getStateList(context: Context): List<StateModel>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getStateList()
        }

        fun getDistrictList(context: Context): List<DistrictModel>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getDistrictList()
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
                    result?.body()!!.last_insert_id
                } else {
                    0
                }
            } catch (e: Exception) {
                Log.d("fail", "$e")
            }
            return 0
        }

        suspend fun userProfileUpdateAPI(userRegistrationTable: UserRegistrationModel): Int {
            try {
                var result = apiInterface?.updateProfile(userRegistrationTable,"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJUSEVfSVNTVUVSIiwiYXVkIjoiVEhFX0FVRElFTkNFIiwiaWF0IjoxNjcwNTg0ODU3LCJuYmYiOjE2NzA1ODQ4NjcsImV4cCI6MTY3MDU4NDk3NywiZGF0YSI6eyJ1c2VyX2lkIjpudWxsLCJ1c2VyX25hbWUiOiJBbWl0IiwibW9iaWxlX251bWJlciI6bnVsbH19.t4EewAXitIlvC8dFadv_dM9TQ6gcyRA6-eex-j2Z-cQ")
                return if (result?.body()?.status==1){
                    result?.body()!!.updated_id
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