package com.indev.claraa.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.indev.claraa.entities.AddressDetailsModel
import com.indev.claraa.roomdb.RoomDB

class OrderPlaceRepository {

    companion object {
        private var dataBase: RoomDB? = null

        private fun initializeDB(context: Context): RoomDB? {
            return RoomDB.getDatabase(context)
        }

        fun getAddress(context: Context, addressId: String): LiveData<AddressDetailsModel>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getAddress(addressId)
        }

    }
}