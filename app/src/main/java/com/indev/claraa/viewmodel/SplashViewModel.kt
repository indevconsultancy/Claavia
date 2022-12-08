package com.indev.claraa.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.indev.claraa.entities.MasterData
import com.indev.claraa.repository.SplashRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SplashViewModel (private val context: Context): ViewModel() {
    val table = arrayOf("state_master", "district_master","product_packet")

    init {
        downloadMasterData()
    }

    fun downloadMasterData() {
        CoroutineScope(Dispatchers.IO).launch {
            for (i in 0 until table.size){
            async {
                SplashRepository.downloadMasterData1(context,MasterData(table[i]))
                }
            }

//            async { SplashRepository.downloadMasterData2(masterDataT2)
////                sharedPrefHelper.setString("download", "yes")
//                context.startActivity(Intent(context, LoginScreen::class.java))
//                (context as AppCompatActivity).finish()
//            }
        }
    }
}