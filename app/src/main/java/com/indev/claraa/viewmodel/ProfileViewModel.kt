package com.indev.claraa.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.indev.claraa.entities.Cart
import com.indev.claraa.entities.UserRegistrationModel
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.CartRepository
import com.indev.claraa.repository.UserRegistrationRepository
import com.indev.claraa.roomdb.RoomDB
import com.indev.claraa.ui.HomeScreen
import com.indev.claraa.ui.UserRegistration

class ProfileViewModel(val context: Context): ViewModel() {


    val readAllData: LiveData<UserRegistrationModel>
    lateinit var prefHelper: PrefHelper

    init {
        readAllData = UserRegistrationRepository.getRegistrationData(context)!!
    }

    fun editBtn(){
        context.startActivity(Intent(context, UserRegistration::class.java))
        prefHelper= PrefHelper(context)
        prefHelper.put( Constant.PREF_IS_LOGIN,true)

    }

}