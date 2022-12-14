package com.indev.claraa.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.*
import com.indev.claraa.entities.CartModel
import com.indev.claraa.entities.ProductMasterModel
import com.indev.claraa.fragment.ProductDetails
import com.indev.claraa.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailViewModel(val context: Context): ViewModel() {

    var packetValue: String? = null
    private lateinit var cartModel: CartModel
    val optionSelectedListener = MutableLiveData<Pair<String, String>>()
    lateinit var productMasterArrayList: ArrayList<ProductMasterModel>


    init{
        var productName= ProductDetails.selectedValue

        CoroutineScope(Dispatchers.IO).launch {
            productMasterArrayList= ProductRepository.getProductData(context,productName) as ArrayList<ProductMasterModel>
        }
    }

    val packetClicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            packetValue = parent?.getItemAtPosition(position) as String
            Toast.makeText(context, "" + packetValue, Toast.LENGTH_LONG).show()
        }
    }

    fun btnSubmit() {

        cartModel = CartModel(0,packetValue.toString(), ProductDetails.rangeValue,productMasterArrayList.get(0).product_id,productMasterArrayList.get(0).product_name,productMasterArrayList.get(0).product_img1,productMasterArrayList.get(0).product_img2,
            productMasterArrayList.get(0).price,"1",productMasterArrayList.get(0).type_id,productMasterArrayList.get(0).packet_id,productMasterArrayList.get(0).power_range,productMasterArrayList.get(0).currency,productMasterArrayList.get(0).active)
        viewModelScope.launch {
            ProductRepository.insertCartData(context ,cartModel)
        }
    }

    fun getCartList(context: Context): LiveData<List<CartModel>>? {
        return ProductRepository.getCartList(context)
    }

    fun getPruductPowerList(context: Context, selectedProduct: String): LiveData<List<ProductMasterModel>>? {
        return ProductRepository.getPowerList(context,selectedProduct)
    }

    fun clickRangeOptionEvent(pair: Pair<String, String>) {
        optionSelectedListener.value = pair
    }
}