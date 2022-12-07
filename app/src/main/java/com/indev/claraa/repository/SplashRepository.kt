package com.indev.claraa.repository

import android.util.Log
import com.indev.claraa.entities.MasterData
import com.indev.claraa.repository.LoginRepository.Companion.apiInterface
import org.json.JSONObject

class SplashRepository {

    suspend fun downloadMasterData(masterData: MasterData) {

        try {
            val result = apiInterface?.downloadMasterData(masterData)
            val jsonObject = JSONObject(result?.body().toString())
            if (jsonObject["success"] == "1") {
                val jsonArray = jsonObject.getJSONArray("tableData")
                Log.d("masterdata",jsonArray.toString())
                if (jsonArray.length()>0){
                    for (i in 0 until jsonArray.length()) {
                        val singleData = JSONObject(jsonArray[i].toString())
//                        val features = Features(0, singleData["feature_id"].toString(), singleData["feature_name"].toString(),
//                            singleData["del_action"].toString(), singleData["created_at"].toString())
//                        masterDao.insertFeatureMasterData(features)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("fail", "$e")
        }
    }

}