package com.indev.claraa.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.indev.claraa.apiResponse.stateMasterResponse
import com.indev.claraa.entities.MasterData
import com.indev.claraa.entities.StateModel
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

            val list = ArrayList<stateMasterResponse>()

            try {
                val result = apiInterface?.downloadMasterData(masterData)

                val jsonArray = JSONArray(result?.body().toString())
                        dataBase?.userDao()?.deleteAllStates()
                        for (i in 0 until jsonArray.length()) {
                            val singleData = JSONObject(jsonArray[i].toString())
                            if(masterData.table_name == "state_master") {
                                val state_master = StateModel(
                                    singleData["state_id"].toString(),
                                    singleData["state_name"].toString(),
                                    singleData["active"].toString()
                                )
                                val state_id =
                                    dataBase?.userDao()?.insertStateMasterData(state_master)
                                Log.e("TAG", "downloadMasterData1: " + state_id + masterData)
                            }
                        }

            } catch (e: Exception) {
                Log.d("fail", "$e")
            }

            //
//                list.addAll(result?.body()!!)
//                for (i in 0 until list.size) {
//                    val state_master = StateModel(list[i].state_id, list[i].state_name, list[i].active)
//                    val state_id=  dataBase?.userDao()?.insertStateMasterData(state_master)
//                    Log.e("TAG", "downloadMasterData1: " + state_id )
//              }

        }

//        suspend fun downloadMasterData2(masterData: MasterData) {
//
//            try {
//                val result = apiInterface?.downloadMasterData(masterData)
//                val jsonObject = JSONObject(result?.body().toString())
//                if (jsonObject["success"] == "1") {
//                    val jsonArray = jsonObject.getJSONArray("tableData")
//                    Log.d("masterdata",jsonArray.toString())
//                    if (jsonArray.length()>0){
//                        for (i in 0 until jsonArray.length()) {
//                            val singleData = JSONObject(jsonArray[i].toString())
//                            val districtModel=DistrictModel(0,0, singleData["district_name"].toString(),0,0)
//                            dataBase?.userDao()?.insertDistrictMasterData(districtModel)
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                Log.d("fail", "$e")
//            }
//        }
    }
}