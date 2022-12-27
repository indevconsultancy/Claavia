package com.indev.claraa.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indev.claraa.CommonClass
import com.indev.claraa.entities.CartModel
import com.indev.claraa.entities.OrderDetailsModel
import com.indev.claraa.entities.OrderMasterModel
import com.indev.claraa.fragment.AddressList
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.AddressDetailsRepository
import com.indev.claraa.repository.PaymentGatewayRepository
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class PaymentGateWayeViewModel (val context: Context): ViewModel() , PaymentResultListener {

    lateinit var cartArrayList: ArrayList<CartModel>
    lateinit var orderMasterModel: OrderMasterModel
    lateinit var orderDetailsModel: OrderDetailsModel
    lateinit var prefHelper: PrefHelper


    init {
        setOrderData()

//        payment()
//        Checkout.preload(context)
    }

    @SuppressLint("NewApi")
    private fun setOrderData() {
        prefHelper = PrefHelper(context)
        var user_id= prefHelper.getString(Constant.PREF_USERID)!!
        CoroutineScope(Dispatchers.IO).launch {
            cartArrayList= PaymentGatewayRepository.getCartList(context) as ArrayList<CartModel>
            var totalAmount= grandTotal(cartArrayList)
            var orderMasterUniqueId= CommonClass.getUniqueId().toString()

            if(cartArrayList.size> 0) {
                orderMasterModel = OrderMasterModel(
                    0,
                    orderMasterUniqueId ,
                    user_id,
                    CommonClass.currentDate().toString(),
                    totalAmount.toString(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "Pending"
                )
                PaymentGatewayRepository.insertOrderMaster(context, orderMasterModel)



                for (i in cartArrayList) {
                    orderDetailsModel = OrderDetailsModel(
                        0,
                        CommonClass.getUniqueId().toString(),
                        orderMasterUniqueId,
                        i.product_id, i.price.toInt(),
                        i.quantity.toInt(),
                        "Pending",
                        i.price.toInt() * i.quantity.toInt(),
                        user_id.toInt(),
                        ""
                    )
                    PaymentGatewayRepository.insertOrderDetails(context, orderDetailsModel)
                }
                callOrderAPI("s")
            }
        }
    }

    private fun grandTotal(size: List<CartModel>): Int {
        var totalPrice = 0
        for (i in size.indices) {
            totalPrice += size[i].amount
        }
        return totalPrice
    }

    fun payment() {
        val amt = 1
        // rounding off the amount.
        val amount = Math.round(amt.toFloat() * 100).toInt()

        // on below line we are
        // initializing razorpay account
        val checkout = Checkout()

        // on the below line we have to see our id.
        checkout.setKeyID("rzp_live_dJgqeHwFE2LXNp")

        // set image
//            checkout.setImage(R.drawable.ic_baseline_add_24)

        // initialize json object
        try {
            val obj = JSONObject()

            // to put name
            obj.put("name", "Amit Kumar")

            // put description
            obj.put("description", "Test payment")

            // to set theme color
            obj.put("theme.color", "")

            // put the currency
            obj.put("currency", "INR")

            // put amount
            obj.put("amount", amount)

//                // put mobile number
//                obj.put("prefill.contact", "9871533499")
//
//                // put email
//                obj.put("prefill.email", "chaitanyamunje@gmail.com")

            val retrObj = JSONObject()
            retrObj.put("enabled", true)
            retrObj.put("max_count", 4)
            obj.put("retry", retrObj)

            // open razorpay to checkout activity
            checkout.open(context as Activity?, obj)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    override fun onPaymentSuccess(s: String?) {
        // this method is called on payment success.
        callOrderAPI(s)
    }

    private fun callOrderAPI(s: String?) {
        viewModelScope.launch {
            var last_id=0
            CoroutineScope(Dispatchers.IO).launch {
                last_id = PaymentGatewayRepository.insertOrderMasterAPI(context,orderMasterModel)
                if (last_id> 0) {
                    for(i in cartArrayList) {
                      var  lasts_id = PaymentGatewayRepository.insertOrderDetailAPI(context,orderDetailsModel)
                        if (lasts_id> 0) {
//                    PaymentGatewayRepository.updateAddressId(last_id,id.toString(), context)
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(context, "No", Toast.LENGTH_LONG).show()
                            }

                        }
                    }
//                    PaymentGatewayRepository.updateAddressId(last_id,id.toString(), context)
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "" +s, Toast.LENGTH_LONG).show()
                    }
                } else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "" + s, Toast.LENGTH_LONG).show()
                    }

                }
            }
        }

    }

    private fun callOrderDetailsAPI() {
        viewModelScope.launch {
            var last_id=0
            CoroutineScope(Dispatchers.IO).launch {

            }
        }
    }

    override fun onPaymentError(p0: Int, s: String?) {
        // on payment failed.
        callOrderAPI(s)

    }
}
