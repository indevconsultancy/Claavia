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


class ProductRepository {
 
    companion object {
        private var dataBase: RoomDB? = null
        lateinit var prefHelper: PrefHelper
        val apiInterface = ClientApi.getClient()?.create(ClaraaApi::class.java)

        private fun initializeDB(context: Context): RoomDB? {
            return RoomDB.getDatabase(context)
        }

        suspend fun insertCartData(context: Context, cartModel: CartModel) {
            dataBase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.insertUserCart(cartModel)
            }

        }

        fun getCartList(context: Context): LiveData<List<CartModel>>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getCartData("Failed", "Pending")
        }

        fun getPowerList(context: Context, selectedProduct: String): LiveData<List<ProductMasterModel>>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getProductPowerList(selectedProduct)
        }


        fun getProductData(context: Context, product_id: Int): List<ProductMasterModel>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getProductData(product_id)
        }

        fun getCartDatabyProductId(productId: Int,context: Context): List<CartModel>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getCartDatabyProductId(productId)
        }

        fun getPacksSize(packet_id: String,context: Context): List<ProductPacketModel>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getPackSizebyID(packet_id)
        }

        fun deleteProductData(cartId: String,context: Context){
            dataBase = initializeDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.deleteByProductId(cartId)
            }
        }

        fun updateCartProductQuantity(quantity: Int,totalPrice: Int, cartId: Int,context: Context){
            dataBase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.updateCartProductQuantity(quantity,totalPrice,cartId)
            }
        }

        fun updateCartProduct(cartModel: CartModel, context: Context){
            dataBase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.updateCartProduct(cartModel)
            }
        }

        fun updateCartId(last_id: Int, id: String, context: Context) {
            dataBase = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.updateCartId(last_id, id)
            }
        }

        suspend fun cartInsertAPI(context: Context,cartModel: CartModel): Int {
            try {
                prefHelper = PrefHelper(context)
                var token="Bearer " +prefHelper.getString(Constant.PREF_TOKEN)
                var result = apiInterface?.cartInsertAPI(cartModel, token!!)
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


        suspend fun cartProductDelete(context: Context,deleteModel: deleteModel): Int {
            try {
                prefHelper = PrefHelper(context)
                var token="Bearer " + prefHelper.getString(Constant.PREF_TOKEN)
                var result = apiInterface?.deleteCartAPI(deleteModel, token!!)
                return if (result?.body()?.status==1){
                    1
                } else {
                    0
                }
            } catch (e: Exception) {
                Log.d("fail", "$e")
            }
            return 0
        }


        suspend fun cartUpdateApi(context: Context,cartModel: CartModel): Int {
            try {
                prefHelper = PrefHelper(context)
                var token="Bearer " + prefHelper.getString(Constant.PREF_TOKEN)
                var result = apiInterface?.cartUpdateAPI(
                    cartModel,token!!)
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

        fun getPacksList(context: Context, packet_id: String): List<ProductPacketModel>? {
           dataBase = initializeDB(context)
            var queryString= "SELECT * FROM product_packet where packet_id in ($packet_id)"
            val query = SimpleSQLiteQuery(queryString)
            return dataBase?.userDao()?.getPacksList(query)
        }
    }
}