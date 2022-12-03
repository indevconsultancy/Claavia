package com.indev.claraa.viewmodel

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.indev.claraa.R
import com.indev.claraa.entities.CartModel
import com.indev.claraa.entities.HomeModel
import com.indev.claraa.fragment.ProductDetails
import com.indev.claraa.repository.CartRepository
import com.indev.claraa.repository.HomeRepository
import com.indev.claraa.ui.HomeScreen

class HomeScreenViewModel (val context: Context): ViewModel() {


    fun cvProduct(){
        replaceFregment(ProductDetails())
    }


    private fun replaceFregment(fragment : Fragment) {
        var transaction = (context as HomeScreen).supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }

//    fun getHomeList(context: Context): LiveData<List<HomeModel>>? {
//        return HomeRepository.getHomeList(context)
//    }


}