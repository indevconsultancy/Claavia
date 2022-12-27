package com.indev.claraa.repository

import android.content.Context
import android.media.session.MediaSession.Token
import android.util.Log
import androidx.lifecycle.LiveData
import com.indev.claraa.entities.AddressDetailsModel
import com.indev.claraa.entities.DistrictModel
import com.indev.claraa.entities.StateModel
import com.indev.claraa.entities.UserRegistrationModel
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
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
        var stateName=""
        lateinit var prefHelper: PrefHelper

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

         fun getsStateName(context: Context, state_id: String): String? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getsStateName(state_id)
        }
        fun getDistrictName(context: Context, district_id: String): String? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getDistrictName(district_id)
        }

        fun updateData(context: Context, userRegistrationTable: UserRegistrationModel){
            dataBase = initializeDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.updateUser(userRegistrationTable)
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

        suspend fun userProfileUpdateAPI(context: Context,userRegistrationTable: UserRegistrationModel): Int {
            try {
                prefHelper = PrefHelper(context)
                var token="Bearer " + prefHelper.getString(Constant.PREF_TOKEN)
                var result = apiInterface?.updateProfile(userRegistrationTable,token!!)
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