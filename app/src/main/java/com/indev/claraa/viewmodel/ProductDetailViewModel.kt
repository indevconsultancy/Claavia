package com.indev.claraa.viewmodel

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.*
import com.indev.claraa.entities.CartModel
import com.indev.claraa.repository.CartRepository
import kotlinx.coroutines.launch

class ProductDetailViewModel(val context: Context): ViewModel() {

    var packetValue: String? = null
    private lateinit var cartModel: CartModel
    val optionSelectedListener = MutableLiveData<Pair<String, String>>()

    val packetClicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            packetValue = parent?.getItemAtPosition(position) as String
            Toast.makeText(context, "" + packetValue, Toast.LENGTH_LONG).show()
        }
    }

    fun btnSubmit() {
        cartModel = CartModel(0,1, packetValue.toString(), "-0.5")
        viewModelScope.launch {
            CartRepository.insertCartData(context ,cartModel)
        }
    }

    fun getCartList(context: Context): LiveData<List<CartModel>>? {
        return CartRepository.getCartList(context)
    }

    fun clickRangeOptionEvent(pair: Pair<String, String>) {
        optionSelectedListener.value = pair
    }
}