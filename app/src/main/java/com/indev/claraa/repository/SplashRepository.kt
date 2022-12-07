package com.indev.claraa.repository

import android.util.Log
import com.indev.claraa.dao.ClaraaDao
import com.indev.claraa.entities.DistrictModel
import com.indev.claraa.entities.MasterData
import com.indev.claraa.entities.StateModel
import com.indev.claraa.restApi.ClaraaApi
import org.json.JSONObject

class SplashRepository (private val apiInterface: ClaraaApi?, private val claraaDao: ClaraaDao){

    suspend fun downloadMasterData1(masterData: MasterData) {

        try {
            val result = apiInterface?.downloadMasterData(masterData)
            val jsonObject = JSONObject(result?.body().toString())
            if (jsonObject["success"] == "1") {
                val jsonArray = jsonObject.getJSONArray("tableData")
                Log.d("masterdata",jsonArray.toString())
                if (jsonArray.length()>0){
                    for (i in 0 until jsonArray.length()) {
                        val singleData = JSONObject(jsonArray[i].toString())
                        val stateModel = StateModel(0,0, singleData["state_name"].toString(),0)
                        claraaDao.insertStateMasterData(stateModel)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("fail", "$e")
        }
    }

    suspend fun downloadMasterData2(masterData: MasterData) {

        try {
            val result = apiInterface?.downloadMasterData(masterData)
            val jsonObject = JSONObject(result?.body().toString())
            if (jsonObject["success"] == "1") {
                val jsonArray = jsonObject.getJSONArray("tableData")
                Log.d("masterdata",jsonArray.toString())
                if (jsonArray.length()>0){
                    for (i in 0 until jsonArray.length()) {
                        val singleData = JSONObject(jsonArray[i].toString())
                        val districtModel=DistrictModel(0,0, singleData["district_name"].toString(),0,0)
                        claraaDao.insertDistrictMasterData(districtModel)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("fail", "$e")
        }
    }

}