package com.indev.claraa.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.indev.claraa.entities.CartModel
import com.indev.claraa.entities.ProductMasterModel
import com.indev.claraa.roomdb.RoomDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductRepository {
 
    companion object {
        private var dataBase: RoomDB? = null

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


        fun getProductData(context: Context, selectedProduct: String): List<ProductMasterModel>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getProductData(selectedProduct)
        }

        fun deleteProductData(cartId: Int,context: Context){
            dataBase = initializeDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.deleteByProductId(cartId)
            }
        }

        fun updateCartProductQuantity(quantity: Int, cartId: Int,context: Context){
            dataBase = initializeDB(context)
            CoroutineScope(Dispatchers.IO).launch {
                dataBase?.userDao()?.updateCartProductQuantity(quantity,cartId)
            }
        }

    }

}