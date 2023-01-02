package com.indev.claraa.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.indev.claraa.entities.*
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.restApi.ClaraaApi
import com.indev.claraa.restApi.ClientApi
import com.indev.claraa.roomdb.RoomDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderHistoryReposetory {
    companion object {
        private var dataBase: RoomDB? = null
        lateinit var prefHelper: PrefHelper
        val apiInterface = ClientApi.getClient()?.create(ClaraaApi::class.java)

        private fun initializeDB(context: Context): RoomDB? {
            return RoomDB.getDatabase(context)
        }


        fun getOrderDetailsList(context: Context): LiveData<List<OrderDetailsModel>>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getOrderDetailsList("Success")
        }
        fun getOrderList(context: Context): LiveData<List<OrderMasterModel>>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getOrderList("Success")
        }

        fun getImage(context: Context, product_id: String): String? {
            dataBase = initializeDB(context)
            var queryString= "SELECT product_img1 FROM product_master where product_id in ($product_id)"
            val query = SimpleSQLiteQuery(queryString)
            return dataBase?.userDao()?.getName(query)
        }

        fun getPowerSize(context: Context, product_id: String): String? {
            dataBase = initializeDB(context)
            var queryString= "SELECT power_range FROM product_master where product_id in ($product_id)"
            val query = SimpleSQLiteQuery(queryString)
            return dataBase?.userDao()?.getName(query)
        }

    fun getProductName(context: Context, product_id: String): String? {
            dataBase = initializeDB(context)
            var queryString= "SELECT product_name FROM product_master where product_id in ($product_id)"
            val query = SimpleSQLiteQuery(queryString)
            return dataBase?.userDao()?.getName(query)
        }

        fun getPacksID(context: Context, product_id: String): String? {
            dataBase = initializeDB(context)
            var queryString= "SELECT packet_id FROM product_master where product_id in ($product_id)"
            val query = SimpleSQLiteQuery(queryString)
            return dataBase?.userDao()?.getName(query)
        }

        fun getPacksSize(context: Context, packet_id: String): String? {
            dataBase = initializeDB(context)
            var queryString= "SELECT packet_size FROM product_packet where packet_id in ($packet_id)"
            val query = SimpleSQLiteQuery(queryString)
            return dataBase?.userDao()?.getName(query)
        }

    }
}