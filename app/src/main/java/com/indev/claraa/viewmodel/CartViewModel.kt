package com.indev.claraa.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.indev.claraa.entities.CartModel
import com.indev.claraa.repository.CartRepository

class CartViewModel (val context: Context): ViewModel() {
    fun getCartList(context: Context): LiveData<List<CartModel>>? {
        return CartRepository.getCartList(context)
    }

}