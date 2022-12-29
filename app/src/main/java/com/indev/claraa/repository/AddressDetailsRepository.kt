package com.indev.claraa.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.indev.claraa.entities.AddressDetailsModel
import com.indev.claraa.entities.DistrictModel
import com.indev.claraa.entities.StateModel
import com.indev.claraa.entities.deleteModel
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.restApi.ClaraaApi
import com.indev.claraa.restApi.ClientApi
import com.indev.claraa.roomdb.RoomDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddressDetailsRepository {
    companion object {
        private var dataBase: RoomDB? = null
        val apiInterface = ClientApi.getClient()?.create(ClaraaApi::class.java)
        lateinit var prefHelper: PrefHelper


        private fun initializeDB(context: Context): RoomDB? {
            return RoomDB.getDatabase(context)
        }

        suspend fun insertAddressData(context: Context, addressDetailsModel: AddressDetailsModel) {
            dataBase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.insertAddressData(addressDetailsModel)
            }
        }

        fun getAddressData(context: Context): LiveData<List<AddressDetailsModel>>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getAddressData()
        }

        fun getAddressDatabyLocalID(context: Context, id: Int): LiveData<AddressDetailsModel>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getAddressDatabyLocalID(id)
        }

        fun editAddress(addressDetailsModel: AddressDetailsModel, context: Context) {
            dataBase = initializeDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.editAddress(addressDetailsModel)
            }
        }

        fun deleteAddress(id: String, context: Context) {
            dataBase = initializeDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.deleteAddress(id)
            }
        }

        fun updateAddressId(last_id: Int, id: String, context: Context) {
            dataBase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.updateAddressId(last_id, id)
            }
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


        suspend fun userAddressDetailsAPI(context: Context,addressDetailsModel: AddressDetailsModel): Int {
            try {
                prefHelper = PrefHelper(context)
                var token="Bearer " + prefHelper.getString(Constant.PREF_TOKEN)
                var result = apiInterface?.addressDetails(addressDetailsModel, token!!)
                return if (result?.body()?.status == 1) {
                    result?.body()!!.last_insert_id
                } else {
                    0
                }
            } catch (e: Exception) {
                Log.d("fail", "$e")
            }
            return 0
        }

        suspend fun addressUpdateApi(context: Context,addressDetailsModel: AddressDetailsModel): Int {
            try {
                prefHelper = PrefHelper(context)
                var token="Bearer " + prefHelper.getString(Constant.PREF_TOKEN)
                var result = apiInterface?.updateAddress(addressDetailsModel, token!!)
                return if (result?.body()?.status == 1) {
                    result?.body()!!.updated_id
                } else {
                    0
                }
            } catch (e: Exception) {
                Log.d("fail", "$e")
            }
            return 0
        }

        suspend fun addressDeleteApi(context: Context,deleteModel: deleteModel): Int {
            try {
                prefHelper = PrefHelper(context)
                var token="Bearer " + prefHelper.getString(Constant.PREF_TOKEN)
                var result = apiInterface?.deleteAddress(
                    deleteModel,token!!)
                return if (result?.body()?.status == 1) {
                   1
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
