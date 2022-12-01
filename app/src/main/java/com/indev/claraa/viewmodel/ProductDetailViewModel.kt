package com.indev.claraa.viewmodel

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indev.claraa.adapter.CartAdapter
import com.indev.claraa.entities.Cart
import com.indev.claraa.entities.ProductMasterModel
import com.indev.claraa.repository.CartRepository
import com.indev.claraa.ui.AnonmyosActivity
import kotlinx.coroutines.launch

class ProductDetailViewModel(val context: Context): ViewModel() {

    var packetValue: String? = null
    var rangeValue: String? = null
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cart: Cart
    private lateinit var productArrayList: ArrayList<ProductMasterModel>
    val optionSelectedListener = MutableLiveData<Pair<String, String>>()


    val packetClicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            packetValue = parent?.getItemAtPosition(position) as String
            Toast.makeText(context, "" + packetValue, Toast.LENGTH_LONG).show()
        }
    }

    val rangeClicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            rangeValue = parent?.getItemAtPosition(position) as String
            Toast.makeText(context, "" + rangeValue, Toast.LENGTH_LONG).show()
        }
    }

    fun btnSubmit() {
        cart = Cart(0, packetValue.toString(), rangeValue.toString())
        viewModelScope.launch {
            CartRepository.insertCartData(context ,cart)
        }
    }

    fun btnOrder() {
        context.startActivity(Intent(context, AnonmyosActivity::class.java))
    }


    fun getCartList(context: Context): LiveData<List<Cart>>? {
        return CartRepository.getCartList(context)
    }


    fun clickRangeOptionEvent(pair: Pair<String, String>) {
        optionSelectedListener.value = pair
    }
//    fun getRangeList(context: Context): LiveData<List<ProductMasterModel>>?  {
//        return productArrayList
//    }
}