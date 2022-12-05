package com.indev.claraa.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.indev.claraa.entities.AddressDetailsModel
import com.indev.claraa.roomdb.RoomDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddressDetailsRepository {
    companion object {
        private var dataBase: RoomDB? = null

        private fun initializeDB(context: Context): RoomDB? {
            return RoomDB.getDatabase(context)
        }

        suspend fun insertAddressData(context: Context, addressDetailsModel: AddressDetailsModel) {
            dataBase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.insertAddressData(addressDetailsModel)
            }
        }

//        fun getAddressData(context: Context): LiveData<UserRegistrationModel>? {
//            AddressDetailsRepository.dataBase = AddressDetailsRepository.initializeDB(context)
//            return AddressDetailsRepository.dataBase?.userDao()?.getAddressData()
//        }
//
//
////        fun getAddressList(context: Context): LiveData<List<AddressDetailsViewModel>>? {
//            dataBase = initializeDB(context)
//            return dataBase?.userDao()?.getAddressData()
//        }


    }
}