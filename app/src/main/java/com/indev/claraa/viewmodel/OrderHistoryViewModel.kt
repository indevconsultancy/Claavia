package com.indev.claraa.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.indev.claraa.entities.CartModel
import com.indev.claraa.entities.OrderDetailsModel
import com.indev.claraa.entities.OrderMasterModel
import com.indev.claraa.repository.OrderHistoryReposetory
import com.indev.claraa.repository.ProductRepository

class OrderHistoryViewModel (val context: Context): ViewModel() {

    fun getOrderDetailsList(context: Context): LiveData<List<OrderDetailsModel>>? {
        return OrderHistoryReposetory.getOrderDetailsList(context)
    }
    fun getOrderList(context: Context): LiveData<List<OrderMasterModel>>? {
        return OrderHistoryReposetory.getOrderList(context)
    }
}