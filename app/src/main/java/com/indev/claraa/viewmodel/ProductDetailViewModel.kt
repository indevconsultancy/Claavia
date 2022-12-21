package com.indev.claraa.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.*
import cn.pedant.SweetAlert.SweetAlertDialog
import com.indev.claraa.adapter.PowerRangeAdapter
import com.indev.claraa.adapter.ProductMasterAdapter
import com.indev.claraa.entities.CartModel
import com.indev.claraa.entities.ProductMasterModel
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.ProductRepository
import com.indev.claraa.roomdb.RoomDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProductDetailViewModel(val context: Context): ViewModel() {

    var packetValue: String? = null
    var qtyValue: String? = null
    private lateinit var cartModel: CartModel
    val optionSelectedListener = MutableLiveData<Pair<String, String>>()
    lateinit var productMasterArrayList: ArrayList<ProductMasterModel>
    lateinit var cartModelArrayList: ArrayList<CartModel>
    private var dataBase: RoomDB? = null
    lateinit var prefHelper: PrefHelper

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
        }
    }

     val qtyClicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            qtyValue = parent?.getItemAtPosition(position) as String
        }
    }


    @SuppressLint("SuspiciousIndentation")
    fun btnSubmit() {
        dataBase = initializeDB(context)
        prefHelper= PrefHelper(context)

        var user_id =prefHelper.getInt(Constant.PREF_USERID)!!
        if(checkValidation()) {
            var checkExitPorduct = 0
            CoroutineScope(Dispatchers.IO).launch {
                var productID = dataBase?.userDao()?.getproductID(PowerRangeAdapter.power_range)!!
                checkExitPorduct = dataBase?.userDao()?.isProductRowExist(
                    productID,
                    PowerRangeAdapter.power_range,
                    packetValue.toString())!!
                var amount= productMasterArrayList.get(0).price.toInt() * qtyValue!!.toInt()
                if (checkExitPorduct == 0) {
                    cartModel = CartModel(
                        0,0,
                        packetValue.toString(),
                        productID.toString(), user_id,
                        productMasterArrayList.get(0).product_name,
                        productMasterArrayList.get(0).product_img1,
                        productMasterArrayList.get(0).product_img2,
                        productMasterArrayList.get(0).price,
                        amount,
                        qtyValue.toString(),
                        productMasterArrayList.get(0).type_id,
                        productMasterArrayList.get(0).packet_id,
                        PowerRangeAdapter.power_range,
                        productMasterArrayList.get(0).currency,
                        "", "", "", productMasterArrayList.get(0).active
                    )
                    viewModelScope.launch {
                        ProductRepository.insertCartData(context, cartModel)
                        Handler(Looper.getMainLooper()).post {
                            showAlertDialog()
                        }
                    }
                } else {
                    cartModelArrayList = ProductRepository.getCartDatabyProductId(
                        productID,
                        context
                    ) as ArrayList<CartModel>
                    var totalAmount =
                        cartModelArrayList.get(0).amount * (cartModelArrayList.get(0).quantity.toInt() + qtyValue!!.toInt())
                    ProductRepository.updateCartProductQuantity(
                        cartModelArrayList.get(0).quantity.toInt() + qtyValue!!.toInt(),
                        totalAmount,
                        cartModelArrayList.get(0).local_id,
                        context
                    )

                    Handler(Looper.getMainLooper()).post {
                        showAlertDialog()
                    }
                }
            }
        }

    }

    private fun showAlertDialog() {
        SweetAlertDialog(context)
            .setTitleText("Added Product in Cart")
            .setContentText("")
            .setConfirmText("Ok")
            .setConfirmClickListener {sdialog ->
                sdialog.dismiss()
            }
            .show()
    }

    private fun checkValidation(): Boolean {
        if (packetValue.toString().trim().equals("Packs Size")) {
            Toast.makeText(context, "Please select Pack size..", Toast.LENGTH_SHORT).show()
            return false
        }

        if (qtyValue.toString().trim().equals("Qty.")) {
            Toast.makeText(context, "Please select Quantity..", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
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