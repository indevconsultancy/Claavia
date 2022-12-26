package com.indev.claraa.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.indev.claraa.entities.AddressDetailsModel
import com.indev.claraa.entities.CartModel
import com.indev.claraa.entities.ProductMasterModel
import com.indev.claraa.entities.UserRegistrationModel
import com.indev.claraa.restApi.ClaraaApi
import com.indev.claraa.restApi.ClientApi
import com.indev.claraa.roomdb.RoomDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductRepository {
 
    companion object {
        private var dataBase: RoomDB? = null
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
            return dataBase?.userDao()?.getCartData()
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

        suspend fun cartInsertAPI(cartModel: CartModel): Int {
            try {
                var result = apiInterface?.cartInsertAPI(cartModel, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJUSEVfSVNTVUVSIiwiYXVkIjoiVEhFX0FVRElFTkNFIiwiaWF0IjoxNjcxODY0MjEyLCJuYmYiOjE2NzE4NjQyMjIsImV4cCI6MTY3NDQ1NjI3MiwiZGF0YSI6eyJ1c2VyX2lkIjpudWxsLCJ1c2VyX25hbWUiOiJBbWl0IiwibW9iaWxlX251bWJlciI6bnVsbH19.Xu-4QUrTmQQpQTlQHr8UfSQxNRTBO4Wb2twDcgd5gCU")
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


        suspend fun cartProductDelete(id: String): Int {
            try {
                var result = apiInterface?.deleteCartAPI(id, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJUSEVfSVNTVUVSIiwiYXVkIjoiVEhFX0FVRElFTkNFIiwiaWF0IjoxNjcxODgwMzM0LCJuYmYiOjE2NzE4ODAzNDQsImV4cCI6MTY3NDQ3MjM5NCwiZGF0YSI6eyJ1c2VyX2lkIjpudWxsLCJ1c2VyX25hbWUiOiJBbWl0IiwibW9iaWxlX251bWJlciI6bnVsbH19.2AOPDJH7mBFeOzxjGKBAFcoXliWfKvDHUjtbt2GYie8")
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


        suspend fun cartUpdateApi(cartModel: CartModel): Int {
            try {
                var result = apiInterface?.cartUpdateAPI(
                    cartModel,
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

    }

}