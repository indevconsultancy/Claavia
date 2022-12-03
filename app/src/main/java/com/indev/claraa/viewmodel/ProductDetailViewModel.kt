package com.indev.claraa.viewmodel

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.*
import com.indev.claraa.adapter.CartAdapter
import com.indev.claraa.entities.CartModel
import com.indev.claraa.entities.ProductMasterModel
import com.indev.claraa.repository.CartRepository
import com.indev.claraa.ui.AnonmyosActivity
import kotlinx.coroutines.launch

class ProductDetailViewModel(val context: Context): ViewModel(), LifecycleOwner {

    var packetValue: String? = null
    var rangeValue: String? = null
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartModel: CartModel
    private lateinit var productArrayList: ArrayList<ProductMasterModel>
    val optionSelectedListener = MutableLiveData<Pair<String, String>>()

    init {

    }

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
        optionSelectedListener.observe(this, Observer { pair ->
            if (pair != null) {
                rangeValue = pair.first
            }
        })

        cartModel = CartModel(0, packetValue.toString(), rangeValue.toString())
        viewModelScope.launch {
            CartRepository.insertCartData(context ,cartModel)
        }
    }

    fun btnOrder() {
        context.startActivity(Intent(context, AnonmyosActivity::class.java))
    }


    fun getCartList(context: Context): LiveData<List<CartModel>>? {
        return CartRepository.getCartList(context)
    }


    fun clickRangeOptionEvent(pair: Pair<String, String>) {
        optionSelectedListener.value = pair
    }

    override fun getLifecycle(): Lifecycle {
        TODO("Not yet implemented")
    }

//    fun getRangeList(context: Context): LiveData<List<ProductMasterModel>>?  {
//        return productArrayList
//    }
}