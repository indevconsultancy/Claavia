package com.indev.claraa.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.indev.claraa.entities.AddressDetailsModel
import com.indev.claraa.entities.UserRegistrationModel
import com.indev.claraa.restApi.ClaraaApi
import com.indev.claraa.restApi.ClientApi
import com.indev.claraa.roomdb.RoomDB
import com.indev.claraa.viewmodel.AddressDetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddressDetailsRepository {
    companion object {
        private var dataBase: RoomDB? = null
        val apiInterface = ClientApi.getClient()?.create(ClaraaApi::class.java)


        private fun initializeDB(context: Context): RoomDB? {
            return RoomDB.getDatabase(context)
        }

        suspend fun insertAddressData(context: Context, addressDetailsModel: AddressDetailsModel) {
            dataBase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.insertAddressData(addressDetailsModel)
            }
        }
//
        fun getAddressData(context: Context): LiveData<List<AddressDetailsModel>>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getAddressData()
        }

        suspend fun userAddressDetailsAPI(addressDetailsModel: AddressDetailsModel): Int {
            try {
                var result = apiInterface?.addressDetails(addressDetailsModel)
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

    }


    }
