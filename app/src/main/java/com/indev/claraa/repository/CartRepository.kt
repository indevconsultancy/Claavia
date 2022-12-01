package com.indev.claraa.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.indev.claraa.entities.Cart
import com.indev.claraa.entities.UserRegistrationModel
import com.indev.claraa.roomdb.RoomDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartRepository {

    companion object {
        private var dataBase: RoomDB? = null

        private fun initializeDB(context: Context): RoomDB? {
            return RoomDB.getDatabase(context)
        }

        suspend fun insertCartData(context: Context, cart: Cart) {
            dataBase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.insertUserCart(cart)
            }

        }

        fun getCartList(context: Context): LiveData<List<Cart>>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getCartData()
        }


    }

}