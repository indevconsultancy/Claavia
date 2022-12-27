package com.indev.claraa.viewmodel

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.indev.claraa.entities.CartModel
import com.indev.claraa.entities.OrderMasterModel
import com.indev.claraa.entities.ProductMasterModel
import com.indev.claraa.repository.PaymentGatewayRepository
import com.indev.claraa.repository.ProductRepository
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


    init {
        setOrderData()

//        payment()
//        Checkout.preload(context)
    }

    private fun setOrderData() {
        CoroutineScope(Dispatchers.IO).launch {
            cartArrayList= PaymentGatewayRepository.getCartList(context) as ArrayList<CartModel>
        }


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
        Toast.makeText(context, "Payment is successful : " + s, Toast.LENGTH_SHORT)
            .show();
    }

    override fun onPaymentError(p0: Int, s: String?) {
        // on payment failed.
        Toast.makeText(context, "Payment Failed due to error : " + s, Toast.LENGTH_SHORT)
            .show();
    }
}
