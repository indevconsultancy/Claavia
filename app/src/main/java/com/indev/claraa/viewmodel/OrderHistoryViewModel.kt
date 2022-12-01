package com.indev.claraa.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.indev.claraa.entities.Cart
import com.indev.claraa.repository.CartRepository

class OrderHistoryViewModel (val context: Context): ViewModel() {
    fun getCartList(context: Context): LiveData<List<Cart>>? {
        return CartRepository.getCartList(context)
    }

}