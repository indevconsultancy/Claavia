package com.indev.claraa.viewmodel

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.indev.claraa.R
import com.indev.claraa.entities.CartModel
import com.indev.claraa.fragment.AddressList
import com.indev.claraa.repository.CartRepository
import com.indev.claraa.ui.HomeScreen

class CartViewModel (val context: Context): ViewModel() {
    fun getCartList(context: Context): LiveData<List<CartModel>>? {
        return CartRepository.getCartList(context)
    }

    fun btnBuy(){
        replaceFregment(AddressList())
    }

    private fun replaceFregment(fragment : Fragment) {
        var transaction = (context as HomeScreen).supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }

}