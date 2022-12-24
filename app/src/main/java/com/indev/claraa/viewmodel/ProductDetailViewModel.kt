package com.indev.claraa.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import cn.pedant.SweetAlert.SweetAlertDialog
import com.indev.claraa.CommonClass
import com.indev.claraa.R
import com.indev.claraa.SweetDialog
import com.indev.claraa.adapter.PowerRangeAdapter
import com.indev.claraa.adapter.ProductMasterAdapter
import com.indev.claraa.entities.CartModel
import com.indev.claraa.entities.ProductMasterModel
import com.indev.claraa.fragment.ProductDetails
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.ProductRepository
import com.indev.claraa.roomdb.RoomDB
import com.indev.claraa.ui.HomeScreen
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
    var etQuantity: ObservableField<String> = ObservableField("")

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


    @SuppressLint("SuspiciousIndentation", "NewApi")
    fun btnSubmit() {
        dataBase = initializeDB(context)
        prefHelper= PrefHelper(context)

        var user_id =prefHelper.getInt(Constant.PREF_USERID)!!
        if(checkValidation()) {
            SweetDialog.showProgressDialog(context)
            var checkExitPorduct = 0
            CoroutineScope(Dispatchers.IO).launch {
                var qty= etQuantity.get().toString()
                var productID = dataBase?.userDao()?.getproductID(PowerRangeAdapter.power_range)!!
                checkExitPorduct = dataBase?.userDao()?.isProductRowExist(
                    productID,
                    PowerRangeAdapter.power_range,
                    packetValue.toString())!!
                var amount= productMasterArrayList.get(0).price.toInt() * qty.toInt()
              var id= CommonClass.getUniqueId().toString()
                if (checkExitPorduct == 0) {
                    cartModel = CartModel(
                        0,id,
                        packetValue.toString(),
                        productID.toString(), user_id,
                        productMasterArrayList.get(0).product_name,
                        productMasterArrayList.get(0).product_img1,
                        productMasterArrayList.get(0).product_img2,
                        productMasterArrayList.get(0).price,
                        amount, qty,
                        productMasterArrayList.get(0).type_id,
                        productMasterArrayList.get(0).packet_id,
                        PowerRangeAdapter.power_range,
                        productMasterArrayList.get(0).currency,
                        "", "", "", productMasterArrayList.get(0).active
                    )
                    viewModelScope.launch {
                        ProductRepository.insertCartData(context, cartModel)
                        var last_inserted_id=0
                        last_inserted_id = ProductRepository.cartInsertAPI(cartModel)
                        if (last_inserted_id> 0) {
                            ProductRepository.updateCartId(last_inserted_id,id,context)
                            Toast.makeText(context, "Added", Toast.LENGTH_LONG).show()
                            showAlertDialog()
                        }else {
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(context, "Something went wrong...", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    SweetDialog.dismissDialog()
                } else {
                    cartModelArrayList = ProductRepository.getCartDatabyProductId(
                        productID,
                        context
                    ) as ArrayList<CartModel>
                    var totalAmount =
                        cartModelArrayList.get(0).amount * (cartModelArrayList.get(0).quantity.toInt() +  qty.toInt())
                    viewModelScope.launch {
                        ProductRepository.updateCartProductQuantity(
                            cartModelArrayList.get(0).quantity.toInt() + qty.toInt(),
                            totalAmount,
                            cartModelArrayList.get(0).local_id,
                            context)
                        var last_updated_id=0
                        last_updated_id = ProductRepository.cartUpdateApi(cartModel)
                        if (last_updated_id> 0) {
                            Toast.makeText(context, "Updated", Toast.LENGTH_LONG).show()
                            showAlertDialog()
                        }else {
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(context, "Something went wrong...", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    SweetDialog.dismissDialog()



                    Handler(Looper.getMainLooper()).post {
                        showAlertDialog()
                    }
                }
            }
        }

    }

    private fun showAlertDialog() {
        SweetAlertDialog(context)
            .setContentText("Added Product successfully in Cart")
            .setConfirmText("Ok")
            .setConfirmClickListener {sdialog ->
                sdialog.dismiss()
                replaceFregment(ProductDetails())
            }
            .show()
    }

    private fun replaceFregment(fragment : Fragment) {
        var transaction = (context as HomeScreen).supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.frame_layout, fragment)
        transaction.commit()
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