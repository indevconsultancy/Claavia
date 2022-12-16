package com.indev.claraa.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.indev.claraa.ui.EmailVarification
import com.indev.claraa.ui.OTPScreen

class MobileNumberViewModel (val context: Context): ViewModel(){

    fun btnMobileNo(){
        context.startActivity(Intent(context, OTPScreen::class.java))

    }
}