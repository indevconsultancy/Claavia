package com.indev.claraa.viewmodel

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.ui.*

class LoginViewModel(val context: Context): ViewModel() {

    var username: ObservableField<String> = ObservableField("")
    var password: ObservableField<String> = ObservableField("")
    lateinit var prefHelper: PrefHelper


    fun signIn(){
//        if (username.get().equals("neha") && password.get().equals("neha123")){
            context.startActivity(Intent(context, HomeScreen::class.java))
//        }
//        else{
//            Toast.makeText(context,"Invalid",Toast.LENGTH_LONG).show()
//        }
    }

    fun registration(){
        prefHelper= PrefHelper(context)
        prefHelper.put( Constant.PREF_IS_LOGIN,false)
        context.startActivity(Intent(context, UserRegistration::class.java))
    }

    fun forgot(){
        context.startActivity(Intent(context, EmailVarification::class.java))
    }

}