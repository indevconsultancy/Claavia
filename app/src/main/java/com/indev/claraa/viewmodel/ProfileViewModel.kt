package com.indev.claraa.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.indev.claraa.entities.UserRegistrationModel
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.UserRegistrationRepository
import com.indev.claraa.roomdb.RoomDB
import com.indev.claraa.ui.UserRegistration
import kotlinx.coroutines.*

class ProfileViewModel(val context: Context): ViewModel() {

    val readAllData: LiveData<UserRegistrationModel>
    lateinit var prefHelper: PrefHelper
    lateinit var database: RoomDB
    var state_name: ObservableField<String> = ObservableField("")
    var district_name: ObservableField<String> = ObservableField("")

    private fun initializeDB(context: Context): RoomDB {
        return RoomDB.getDatabase(context)
    }

    init {
        readAllData = UserRegistrationRepository.getRegistrationData(context)!!
    }

    fun getState(stateId: String) {
        CoroutineScope(Dispatchers.IO).launch {
           var  stat_name= UserRegistrationRepository.getsStateName(context, stateId)!!
            state_name.set(stat_name)
        }
    }
    fun getDistrict(districtId: String){
        CoroutineScope(Dispatchers.IO).launch {
            var districtName = UserRegistrationRepository.getDistrictName(context, districtId)!!
            district_name.set(districtName)
        }

    }

//    fun getsStateName(state_id : String): String {
//        database= initializeDB(context)
//        CoroutineScope(Dispatchers.IO).launch {
//            stat_name = database.userDao().getsStateName("1")
//            Log.e("TAG", "getsStateName: " + stat_name)
//        }
//        return stat_name
//    }

    fun editBtn(){
        context.startActivity(Intent(context, UserRegistration::class.java))
        prefHelper= PrefHelper(context)
        prefHelper.put( Constant.PREF_IS_LOGIN,true)
    }

}