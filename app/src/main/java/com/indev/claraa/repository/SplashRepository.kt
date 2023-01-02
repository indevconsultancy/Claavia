package com.indev.claraa.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.indev.claraa.entities.*
import com.indev.claraa.restApi.ClaraaApi
import com.indev.claraa.restApi.ClientApi
import com.indev.claraa.roomdb.RoomDB
import org.json.JSONArray
import org.json.JSONObject

class SplashRepository{


    companion object {
        private var dataBase: RoomDB? = null
        val apiInterface = ClientApi.getClient()?.create(ClaraaApi::class.java)

        private fun initializeDB(context: Context): RoomDB? {
            return RoomDB.getDatabase(context)
        }



        @SuppressLint("SuspiciousIndentation")
        suspend fun downloadMasterData1(context: Context, masterData: MasterData) {
            dataBase = initializeDB(context)

            try {
                val result = apiInterface?.downloadMasterData(masterData)
                Log.e("TAG", "downloadMasterData1: "+ masterData.table_name)
                val jsonArray = JSONArray(result?.body().toString())

                        for (i in 0 until jsonArray.length()) {
                            val singleData = JSONObject(jsonArray[i].toString())
                             if(masterData.table_name == "product_packet") {
                                val product_packet = ProductPacketModel(
                                    singleData["packet_id"].toString(),
                                    singleData["packet_size"].toString(),
                                    singleData["active"].toString()
                                )
                                dataBase?.userDao()?.insertProductPacketMasterData(product_packet)
                            }else if(masterData.table_name == "product_type") {
                                val product_type_master = ProductTypeModel(
                                    singleData["type_id"].toString(),
                                    singleData["type_name"].toString(),
                                    singleData["active"].toString())
                                val type_id=  dataBase?.userDao()?.insertProductTypeData(product_type_master)
                                Log.e("TAG", "downloadProductMasterData: " +type_id)
                            }else if(masterData.table_name == "state_master") {
                                val state_master = StateModel(
                                    singleData["state_id"].toString(),
                                    singleData["state_name"].toString(),
                                    singleData["active"].toString()
                                )
                                dataBase?.userDao()?.insertStateMasterData(state_master)
                            }else if(masterData.table_name == "district_master") {
                                val district_master = DistrictModel(
                                    singleData["district_id"].toString(),
                                    singleData["district_name"].toString(),
                                    singleData["active"].toString(),
                                    singleData["state_id"].toString()
                                )
                                dataBase?.userDao()?.insertDistrictMasterData(district_master)
                            }else if(masterData.table_name == "product_master") {
                                val product_master = ProductMasterModel(
                                    singleData["product_id"].toString(),
                                    singleData["product_name"].toString(),
                                    singleData["product_img1"].toString(),
                                    singleData["product_img2"].toString(),
                                    singleData["price"].toString(),
                                    singleData["type_id"].toString(),
                                    singleData["packet_id"].toString(),
                                    singleData["power_range"].toString(),
                                    singleData["currency"].toString(),
                                    singleData["active"].toString())
                                val produc_id=  dataBase?.userDao()?.insertProductMasterData(product_master)
                                Log.e("TAG", "downloadProductMasterData: " +produc_id)
                            }
                        }

            } catch (e: Exception) {
                Log.d("fail", "$e")
            }
        }

        fun deleteTables(context: Context) {
            dataBase = initializeDB(context)

            dataBase?.userDao()?.deleteAllStates()
            dataBase?.userDao()?.deleteAllDistricts()
            dataBase?.userDao()?.deleteAllProductPackets()
            dataBase?.userDao()?.deleteAllProductMaster()
            dataBase?.userDao()?.deleteAllProductType()
        }
    }
}