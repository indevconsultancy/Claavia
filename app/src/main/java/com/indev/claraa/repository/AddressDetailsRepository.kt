package com.indev.claraa.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.indev.claraa.entities.AddressDetailsModel
import com.indev.claraa.entities.DistrictModel
import com.indev.claraa.entities.StateModel
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



        suspend fun userAddressDetailsAPI(addressDetailsModel: AddressDetailsModel): Int {
            try {
                var result = apiInterface?.addressDetails(
                    addressDetailsModel,
                    "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJUSEVfSVNTVUVSIiwiYXVkIjoiVEhFX0FVRElFTkNFIiwiaWF0IjoxNjcxNTI1NzYzLCJuYmYiOjE2NzE1MjU3NzMsImV4cCI6MTY3NDExNzgyMywiZGF0YSI6eyJ1c2VyX2lkIjpudWxsLCJ1c2VyX25hbWUiOiJBbWl0IiwibW9iaWxlX251bWJlciI6bnVsbH19.dPRrCfHsmpVPS0Rr0HquCzUca9qJOyUhy52HZoZzO1o"
                )
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

        suspend fun addressUpdateApi(addressDetailsModel: AddressDetailsModel): Int {
            try {
                var result = apiInterface?.updateAddress(
                    addressDetailsModel,
                    "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJUSEVfSVNTVUVSIiwiYXVkIjoiVEhFX0FVRElFTkNFIiwiaWF0IjoxNjcxNjEzMzk5LCJuYmYiOjE2NzE2MTM0MDksImV4cCI6MTY3NDIwNTQ1OSwiZGF0YSI6eyJ1c2VyX2lkIjpudWxsLCJ1c2VyX25hbWUiOiJBbWl0IiwibW9iaWxlX251bWJlciI6bnVsbH19.kgPPS_tGELwddw0hW3UwwQtW0-ZNZvza2R8FE0XJxr8"
                )
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

        suspend fun addressDeleteApi(id: String): Int {
            try {
                var result = apiInterface?.deleteAddress(
                    id,
                    "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJUSEVfSVNTVUVSIiwiYXVkIjoiVEhFX0FVRElFTkNFIiwiaWF0IjoxNjcxNjEzMzk5LCJuYmYiOjE2NzE2MTM0MDksImV4cCI6MTY3NDIwNTQ1OSwiZGF0YSI6eyJ1c2VyX2lkIjpudWxsLCJ1c2VyX25hbWUiOiJBbWl0IiwibW9iaWxlX251bWJlciI6bnVsbH19.kgPPS_tGELwddw0hW3UwwQtW0-ZNZvza2R8FE0XJxr8"
                )
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
