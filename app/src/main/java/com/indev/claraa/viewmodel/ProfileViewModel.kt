package com.indev.claraa.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indev.claraa.entities.StateModel
import com.indev.claraa.entities.UserRegistrationModel
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.UserRegistrationRepository
import com.indev.claraa.roomdb.RoomDB
import com.indev.claraa.ui.UserRegistration
import kotlinx.coroutines.*
import kotlin.math.log

class ProfileViewModel(val context: Context): ViewModel() {


    val readAllData: LiveData<UserRegistrationModel>
    lateinit var prefHelper: PrefHelper
    lateinit var database: RoomDB
    var stat_name=""


    private fun initializeDB(context: Context): RoomDB {
        return RoomDB.getDatabase(context)
    }

    init {
        readAllData = UserRegistrationRepository.getRegistrationData(context)!!
        getsStateName("")
    }

    fun getState(): String {
        var state_name=""
        CoroutineScope(Dispatchers.IO).launch {
          state_name= UserRegistrationRepository.getsStateName(context, "1")!!
        }
        return state_name
    }

    fun getsStateName(state_id : String): String {
        database= initializeDB(context)
        CoroutineScope(Dispatchers.IO).launch {
            stat_name = database.userDao().getsStateName("1")
            Log.e("TAG", "getsStateName: " + stat_name)
        }
        return stat_name
    }

    fun editBtn(){
        context.startActivity(Intent(context, UserRegistration::class.java))
        prefHelper= PrefHelper(context)
        prefHelper.put( Constant.PREF_IS_LOGIN,true)
    }

}