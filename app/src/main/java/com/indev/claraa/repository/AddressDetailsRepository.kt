package com.indev.claraa.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.indev.claraa.entities.AddressDetailsModel
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
                var result = apiInterface?.addressDetails(addressDetailsModel,"Bearer :eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJUSEVfSVNTVUVSIiwiYXVkIjoiVEhFX0FVRElFTkNFIiwiaWF0IjoxNjcwNTg0ODU3LCJuYmYiOjE2NzA1ODQ4NjcsImV4cCI6MTY3MDU4NDk3NywiZGF0YSI6eyJ1c2VyX2lkIjpudWxsLCJ1c2VyX25hbWUiOiJBbWl0IiwibW9iaWxlX251bWJlciI6bnVsbH19.t4EewAXitIlvC8dFadv_dM9TQ6gcyRA6-eex-j2Z-cQ")
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
