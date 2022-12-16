package com.indev.claraa.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.indev.claraa.ui.UserRegistration

class RegisterWithUsViewModel (val context: Context): ViewModel(){

    fun registration(){
        context.startActivity(Intent(context, UserRegistration::class.java))
    }
}