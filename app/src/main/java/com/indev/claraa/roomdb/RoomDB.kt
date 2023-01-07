package com.indev.claraa.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.indev.claraa.dao.ClaraaDao
import com.indev.claraa.entities.*

const val TYPE_ITEM =1
const val TYPE_HEADER= 0

@Database(entities = [UserRegistrationModel::class,
                        CartModel::class,
                        AddressDetailsModel::class,
                        StateModel::class,
                        DistrictModel::class,
                        ProductMasterModel::class,
                        ProductTypeModel::class,
                        ProductPacketModel::class,
                        OrderMasterModel::class,SliderModel::class,
                        OrderDetailsModel::class], version = 2)

abstract class RoomDB: RoomDatabase() {

    abstract fun userDao(): ClaraaDao

    companion object {
        @Volatile
        private var INSTANCE: RoomDB? = null

        fun getDatabase(context: Context): RoomDB {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        RoomDB::class.java,
                        "claraa.db"
                    ).fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}
