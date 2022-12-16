package com.indev.claraa.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.*
import com.indev.claraa.adapter.PowerRangeAdapter
import com.indev.claraa.adapter.ProductMasterAdapter
import com.indev.claraa.entities.CartModel
import com.indev.claraa.entities.ProductMasterModel
import com.indev.claraa.fragment.ProductDetails
import com.indev.claraa.repository.ProductRepository
import com.indev.claraa.repository.SplashRepository
import com.indev.claraa.roomdb.RoomDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailViewModel(val context: Context): ViewModel() {

    var packetValue: String? = null
    private lateinit var cartModel: CartModel
    val optionSelectedListener = MutableLiveData<Pair<String, String>>()
    lateinit var productMasterArrayList: ArrayList<ProductMasterModel>
    lateinit var cartModelArrayList: ArrayList<CartModel>
    private var dataBase: RoomDB? = null

    private fun initializeDB(context: Context): RoomDB? {
        return RoomDB.getDatabase(context)
    }

    init{
        var product_id= ProductMasterAdapter.productId

        CoroutineScope(Dispatchers.IO).launch {
            productMasterArrayList= ProductRepository.getProductData(context,product_id.toInt()) as ArrayList<ProductMasterModel>
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
        dataBase = initializeDB(context)

        var checkExitPorduct=0
        CoroutineScope(Dispatchers.IO).launch {
            var productID = dataBase?.userDao()?.getproductID(PowerRangeAdapter.power_range)!!
            checkExitPorduct = dataBase?.userDao()?.isProductRowExist(productID, productMasterArrayList.get(0).power_range, packetValue.toString())!!
            if(checkExitPorduct==0) {
                cartModel = CartModel(
                    0,
                    packetValue.toString(),
                    productID.toString(),"",
                    productMasterArrayList.get(0).product_name,
                    productMasterArrayList.get(0).product_img1,
                    productMasterArrayList.get(0).product_img2,
                    productMasterArrayList.get(0).price,
                    productMasterArrayList.get(0).price.toInt(),
                    "1",
                    productMasterArrayList.get(0).type_id,
                    productMasterArrayList.get(0).packet_id,
                    PowerRangeAdapter.power_range,
                    productMasterArrayList.get(0).currency,
                    "", "",productMasterArrayList.get(0).active)
                    viewModelScope.launch {
                    ProductRepository.insertCartData(context, cartModel)
                }
            }else{
                cartModelArrayList= ProductRepository.getCartDatabyProductId(productMasterArrayList.get(0).product_id.toInt(),context) as ArrayList<CartModel>
                var totalAmount= cartModelArrayList.get(0).amount * (cartModelArrayList.get(0).quantity.toInt()+1)
                ProductRepository.updateCartProductQuantity(cartModelArrayList.get(0).quantity.toInt() + 1,totalAmount,cartModelArrayList.get(0).id,context)
            }
        }

    }

    fun getCartList(context: Context): LiveData<List<CartModel>>? {
        return ProductRepository.getCartList(context)
    }

    fun getPruductPowerList(context: Context, selectedProduct: String): LiveData<List<ProductMasterModel>>? {
        return ProductRepository.getPowerList(context,selectedProduct)
    }
//
//    fun clickRangeOptionEvent(pair: Pair<String, String>) {
//        optionSelectedListener.value = pair
//    }
}