package com.indev.claraa.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.indev.claraa.ui.ProductDetail

class HomeScreenViewModel (val context: Context): ViewModel() {

    fun cvImg(){
        context.startActivity(Intent(context, ProductDetail::class.java))
    }
    fun cvProduct(){
        context.startActivity(Intent(context, ProductDetail::class.java))
    }

}