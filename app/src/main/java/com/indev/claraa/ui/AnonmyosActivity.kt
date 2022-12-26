package com.indev.claraa.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.indev.claraa.R
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONException
import org.json.JSONObject

class AnonmyosActivity : AppCompatActivity(), PaymentResultListener {


    lateinit var btnPay: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anonmyos)
        btnPay = findViewById(R.id.btnPay)
        btnPay.setOnClickListener {
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
                retrObj.put("enabled",true)
                retrObj.put("max_count",4)
                obj.put("retry",retrObj)

                // open razorpay to checkout activity
                checkout.open(this@AnonmyosActivity, obj)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        Checkout.preload(applicationContext)
    }

    override fun onPaymentSuccess(s: String?) {
        // this method is called on payment success.
        Toast.makeText(applicationContext, "Payment is successful : " + s, Toast.LENGTH_SHORT)
            .show();
    }

    override fun onPaymentError(p0: Int, s: String?) {
        // on payment failed.
        Toast.makeText(applicationContext, "Payment Failed due to error : " + s, Toast.LENGTH_SHORT)
            .show();
    }
}
