package com.indev.claraa.viewmodel

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.indev.claraa.entities.MasterData
import com.indev.claraa.repository.SplashRepository
import com.indev.claraa.ui.LoginScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.prefs.Preferences

class SplashViewModel (private val context: Context, private val splashRepository: SplashRepository): ViewModel() {
   // val preferences:Preferences(context)
    val table = arrayOf("state_master", "district_master")

    init {
        downloadMasterData()
    }

    fun downloadMasterData() {
        val masterDataT1 = MasterData(table[0])
        val masterDataT2 = MasterData(table[1])
        CoroutineScope(Dispatchers.IO).launch {
            async { splashRepository.downloadMasterData1(masterDataT1)  }
            async { splashRepository.downloadMasterData2(masterDataT2)
//                sharedPrefHelper.setString("download", "yes")
                context.startActivity(Intent(context, LoginScreen::class.java))
                (context as AppCompatActivity).finish()
            }
        }
    }
}