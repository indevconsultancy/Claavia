package com.indev.claraa.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.indev.claraa.entities.AddressDetailsModel
import com.indev.claraa.entities.CartModel
import com.indev.claraa.roomdb.RoomDB

class PaymentGatewayRepository {

    companion object {
        private var dataBase: RoomDB? = null

        private fun initializeDB(context: Context): RoomDB? {
            return RoomDB.getDatabase(context)
        }

        fun getCartList(context: Context): LiveData<List<CartModel>>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getCartData()
        }
    }
}