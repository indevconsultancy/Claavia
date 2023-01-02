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
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R
import com.indev.claraa.SweetDialog
import com.indev.claraa.adapter.PowerRangeAdapter
import com.indev.claraa.adapter.ProductMasterAdapter
import com.indev.claraa.entities.CartModel
import com.indev.claraa.entities.ProductMasterModel
import com.indev.claraa.entities.ProductPacketModel
import com.indev.claraa.fragment.ProductDetails
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.ProductRepository
import com.indev.claraa.roomdb.RoomDB
import com.indev.claraa.ui.HomeScreen
import com.indev.claraa.util.CommonClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Objects


class ProductDetailViewModel(val context: Context): ViewModel(), ClickLinstener{

    var packetValue: String? = null
    var qtyValue: String? = null
    private lateinit var cartModel: CartModel
    val optionSelectedListener = MutableLiveData<Pair<String, String>>()
    lateinit var productMasterArrayList: ArrayList<ProductMasterModel>
    lateinit var cartModelArrayList: ArrayList<CartModel>
    private var dataBase: RoomDB? = null
    lateinit var prefHelper: PrefHelper
    var etQuantity: ObservableField<String> = ObservableField("")
    var user_id="0"
    var isSubmitButtonEnabled = true
    var product_id=0

    private fun initializeDB(context: Context): RoomDB? {
        return RoomDB.getDatabase(context)
    }

    init{
         product_id=ProductMasterAdapter.productId.toInt()

        CoroutineScope(Dispatchers.IO).launch {
            productMasterArrayList= ProductRepository.getProductData(context,product_id.toInt()) as ArrayList<ProductMasterModel>

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
        if(isSubmitButtonEnabled ==true) {
            dataBase = initializeDB(context)
            prefHelper = PrefHelper(context)
            user_id = prefHelper.getString(Constant.PREF_USERID)!!

            if(prefHelper.getString(Constant.PREF_PRODUCT_NAME)!!.contains("Solution-")== true){
                packetValue = productMasterArrayList.get(0).packet_id
            }else{
                packetValue = ProductDetails.packet_id.toString()
            }
            if (checkValidation()) {
                isSubmitButtonEnabled = false
                SweetDialog.showProgressDialog(context)
                var checkExitPorduct = 0
                CoroutineScope(Dispatchers.IO).launch {
                    var qty = etQuantity.get().toString()
                    var productID =
                        dataBase?.userDao()?.getproductID(PowerRangeAdapter.power_range)!!
                    checkExitPorduct = dataBase?.userDao()?.isProductRowExist(
                        productID,
                        PowerRangeAdapter.power_range,
                        packetValue.toString(), "Pending"
                    )!!

                    if (checkExitPorduct == 0) {
                        insertCart(product_id, qty)
                        SweetDialog.dismissDialog()
                    } else {
                        updateCart(product_id, qty)
                    }
                }

            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun updateCart(productID: Int, qty: String) {
        cartModelArrayList = ProductRepository.getCartDatabyProductId(
            productID,
            context
        ) as ArrayList<CartModel>
        var totalAmount = cartModelArrayList.get(0).price.toInt() * (cartModelArrayList.get(0).quantity.toInt() +  qty.toInt())
        viewModelScope.launch {
            cartModel = CartModel(
                cartModelArrayList.get(0).local_id,cartModelArrayList.get(0).id,
                packetValue.toString(),
                productID.toString(), user_id,
                productMasterArrayList.get(0).product_name,
                productMasterArrayList.get(0).product_img1,
                productMasterArrayList.get(0).product_img2,
                productMasterArrayList.get(0).price,
                totalAmount,
                (cartModelArrayList.get(0).quantity.toInt() + qty.toInt()).toString(),
                productMasterArrayList.get(0).type_id,
                packetValue.toString(),
                PowerRangeAdapter.power_range,
                productMasterArrayList.get(0).currency,
                CommonClass.currentDate().toString(), prefHelper.getString(Constant.PREF_LATITUDE).toString(),prefHelper.getString(Constant.PREF_LONGITUDE).toString(),"Pending", productMasterArrayList.get(0).active
            )

            /*   ProductRepository.updateCartProductQuantity(
                            cartModelArrayList.get(0).quantity.toInt() + qty.toInt(),
                            totalAmount,
                            cartModelArrayList.get(0).local_id,
                            context)*/
            ProductRepository.updateCartProduct(cartModel, context)
            var last_updated_id=0
            last_updated_id = ProductRepository.cartUpdateApi(context,cartModel)
            if (last_updated_id> 0) {
                Handler(Looper.getMainLooper()).post{
                    showAlertDialog("Updated Product successfully in Cart")
                }
            }else {
                Handler(Looper.getMainLooper()).post{
                    Toast.makeText(context, "Something went wrong...", Toast.LENGTH_LONG).show()
                }
        }
        }
        SweetDialog.dismissDialog()
    }

    @SuppressLint("NewApi")
    private fun insertCart(productID: Int, qty: String) {
        var amount= productMasterArrayList.get(0).price.toInt() * qty.toInt()
        var id= CommonClass.getUniqueId().toString()
        cartModel = CartModel(
            0,id,
            packetValue.toString(),
            productID.toString(), user_id,
            productMasterArrayList.get(0).product_name,
            productMasterArrayList.get(0).product_img1,
            productMasterArrayList.get(0).product_img2,
            productMasterArrayList.get(0).price,
            amount,
            qty,
            productMasterArrayList.get(0).type_id,
            packetValue.toString(),
            PowerRangeAdapter.power_range,
            productMasterArrayList.get(0).currency,
            CommonClass.currentDate().toString(), prefHelper.getString(Constant.PREF_LATITUDE).toString(),prefHelper.getString(Constant.PREF_LONGITUDE).toString(),"Pending", productMasterArrayList.get(0).active
        )
        viewModelScope.launch {
            ProductRepository.insertCartData(context, cartModel)
            var last_inserted_id=0
            last_inserted_id = ProductRepository.cartInsertAPI(context,cartModel)
            if (last_inserted_id> 0) {
                ProductRepository.updateCartId(last_inserted_id,id,context)
                showAlertDialog("Added Product successfully in Cart")
            }else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "Something went wrong...", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun showAlertDialog(s: String) {
        SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
            .setContentText(s)
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

        if(prefHelper.getString(Constant.PREF_PRODUCT_NAME)?.contains("Solution-")== false){
            if (packetValue.toString().trim().equals("0")) {
                Toast.makeText(context, "Please select pack size..", Toast.LENGTH_SHORT).show()
                return false
            }
        }


        if (etQuantity.get().toString().trim().isEmpty() ==true) {
            Toast.makeText(context, "Please select Quantity..", Toast.LENGTH_SHORT).show()
            return false
        }else if (etQuantity.get()?.toInt()!! < 1 || etQuantity.get()?.toInt()!! > 999) {
            Toast.makeText(context, "Please enter quantity more then 1 and less than 999", Toast.LENGTH_SHORT).show()
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

    fun getPacksList(context: Context, packet_id: String): List<ProductPacketModel>? {
        return ProductRepository.getPacksList(context, packet_id)
    }

    override fun onClickListner(position: Int) {
    }

    override fun updateTextView(amount: Int) {
    }

    override fun updatePowerRange(power_range: String) {
    }

    override fun callUpdateCart(id: Int, qty: String) {
        updateCart(id, qty)
    }



//
//    fun clickRangeOptionEvent(pair: Pair<String, String>) {
//        optionSelectedListener.value = pair
//    }
}