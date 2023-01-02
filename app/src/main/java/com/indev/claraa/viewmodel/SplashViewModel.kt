package com.indev.claraa.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.indev.claraa.entities.MasterData
import com.indev.claraa.entities.ProductMasterModel
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.SplashRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SplashViewModel (private val context: Context): ViewModel() {
    val table = arrayOf("product_packet","product_master","product_type","state_master", "district_master")
    lateinit var productMasterModel: ProductMasterModel
    lateinit var prefHelper: PrefHelper

    init {
        prefHelper= PrefHelper(context)
        var checkSplash= prefHelper.getBoolean(Constant.PREF_SPLASH)
        if(checkSplash == false) {
            downloadMasterData()
        }
    }

    fun downloadMasterData() {
        CoroutineScope(Dispatchers.IO).launch {
            SplashRepository.deleteTables(context)
            for (i in 0 until table.size){
                async { SplashRepository.downloadMasterData1(context,MasterData(table[i])) }
            }
        }
    }
}