package com.indev.claraa.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.indev.claraa.entities.ProductMasterModel
import com.indev.claraa.entities.SliderModel
import com.indev.claraa.roomdb.RoomDB

class HomeRepository {
    companion object {
        private var dataBase: RoomDB? = null

        private fun initializeDB(context: Context): RoomDB? {
            return RoomDB.getDatabase(context)
        }

        fun getProductList(context: Context, selectedCategory: Int): LiveData<List<ProductMasterModel>>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getProductMasterData(selectedCategory)
        }


        fun getSliderList(context: Context): LiveData<List<SliderModel>>? {
            dataBase = initializeDB(context)
            return dataBase?.userDao()?.getSliderData()
        }


    }
}