package com.indev.claraa.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
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

        fun getDistrictList(context: Context, state_id: Int): List<DistrictModel>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getDistrictList(state_id)
        }

//         fun getsStateName(context: Context, state_id: String): String? {
//            dataBase = initializeDB(context)
//            return dataBase?.userDao()?.getsStateName(state_id)
//        }
//        fun getDistrictName(context: Context, district_id: String): String? {
//            dataBase = initializeDB(context)
//            return dataBase?.userDao()?.getDistrictName(district_id)
//        }

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
                    result?.body()!!.status
                }else if (result?.body()?.status==2){
                    result?.body()!!.status
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

        fun getStateName(context: Context, state_id: String): String? {
            dataBase = initializeDB(context)
            var queryString= "SELECT state_name FROM state_master where state_id =$state_id"
            val query = SimpleSQLiteQuery(queryString)
            return dataBase?.userDao()?.getName(query)
        }

        fun getDistrictName(context: Context, district_id: String): String? {
            dataBase = initializeDB(context)
            var queryString= "SELECT district_name FROM district_master where district_id =$district_id"
            val query = SimpleSQLiteQuery(queryString)
            return dataBase?.userDao()?.getName(query)
        }

    }
}