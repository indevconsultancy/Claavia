package com.indev.claraa.repository

import android.content.Context
import com.indev.claraa.roomdb.RoomDB

class HomeRepository {
    companion object {
        private var dataBase: RoomDB? = null

        private fun initializeDB(context: Context): RoomDB? {
            return RoomDB.getDatabase(context)
        }

//        suspend fun insertCartData(context: Context, homeModel: HomeModel) {
//            dataBase = initializeDB(context)
//
//            CoroutineScope(Dispatchers.IO).launch {
//                dataBase?.userDao()?.insertHome(homeModel)
//            }
//
//        }

//        fun getHomeList(context: Context): LiveData<List<HomeModel>>? {
//            dataBase = initializeDB(context)
//            return dataBase?.userDao()?.getHomeData()
//        }


    }
}