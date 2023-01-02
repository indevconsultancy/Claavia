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

class PaymentGatewayRepository {

    companion object {
        private var dataBase: RoomDB? = null
        lateinit var prefHelper: PrefHelper
        val apiInterface = ClientApi.getClient()?.create(ClaraaApi::class.java)

        private fun initializeDB(context: Context): RoomDB? {
            return RoomDB.getDatabase(context)
        }

        fun getCartPendingList(context: Context): List<CartModel>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getCartList("Pending")
        }

        fun getCartList(context: Context): List<CartModel>? {
            dataBase = initializeDB(context)
            var queryString= "SELECT * FROM cart where payment_status=  \"Failed\"  or payment_status =  \"Pending\"   ORDER BY local_id ASC"
            val query = SimpleSQLiteQuery(queryString)
            return dataBase?.userDao()?.getCartList(query)
        }


        fun getOrderDetailsList(context: Context, order_id: Int): List<OrderDetailsModel>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getOrderDetailsList(order_id,"Pending")
        }

        fun insertOrderMaster(context: Context, orderMasterModel: OrderMasterModel){
            dataBase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.insertOrderMaster(orderMasterModel)
            }
        }

        suspend fun insertOrderDetails(context: Context, orderDetailsModel: OrderDetailsModel) {
            dataBase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.insertOrderDetails(orderDetailsModel)
            }
        }

        suspend fun insertOrderMasterAPI(context: Context,orderMasterModel: OrderMasterModel): Int {
            try {
                prefHelper = PrefHelper(context)
                var token="Bearer " + prefHelper.getString(Constant.PREF_TOKEN)
                var result = apiInterface?.insertOrderMasterAPI(orderMasterModel, token!!)
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

        suspend fun updateOrderMasterAPI(context: Context,orderMasterModel: OrderMasterModel): Int {
            try {
                prefHelper = PrefHelper(context)
                var token="Bearer " + prefHelper.getString(Constant.PREF_TOKEN)
                var result = apiInterface?.updateOrderMasterAPI(orderMasterModel, token!!)
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

        suspend fun updateOrderDetailsAPI(context: Context,orderDetailsModel: OrderDetailsModel): Int {
            try {
                prefHelper = PrefHelper(context)
                var token="Bearer " + prefHelper.getString(Constant.PREF_TOKEN)
                var result = apiInterface?.updateOrderDetailsAPI(orderDetailsModel, token!!)
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

        suspend fun insertOrderDetailAPI(context: Context,orderDetailsModel: OrderDetailsModel): Int {
            try {
                prefHelper = PrefHelper(context)
                var token="Bearer " + prefHelper.getString(Constant.PREF_TOKEN)
                var result = apiInterface?.insertOrderDetailAPI(orderDetailsModel, token!!)
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


        fun updateOrderMasterbyId(last_id: Int, local_id: Int, context: Context) {
           dataBase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.updateOrderMasterbyId(last_id, local_id)
            }
        }

        fun updateOrderDetailsbyId(last_id: Int, local_id: Int, context: Context) {
           dataBase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.updateOrderDetailsbyId(last_id, local_id)
            }
        }

        fun updateOrderMaster(context: Context, orderMasterModel: OrderMasterModel) {
            dataBase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.updateOrderMaster(orderMasterModel)
            }
        }

        fun updateOrderDetails(context: Context, orderDetailsModel: OrderDetailsModel) {
            dataBase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.updateOrderDetails(orderDetailsModel)
            }
        }

        fun updateCartPaymentStatusbyId(cart_id: Int,payment_status: String, context: Context) {
            dataBase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.updateCartPaymentStatusbyId(cart_id, payment_status)
            }
        }

        fun updateCreditInUserMaster(user_id: Int,credit: String, context: Context) {
            dataBase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.updateCreditUserMaster(user_id, credit)
            }
        }
    }
}