package com.indev.claraa.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.indev.claraa.R
import com.indev.claraa.databinding.ActivityPaymentGatewayBinding
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.viewmodel.PaymentGateWayeViewModel
import com.indev.claraa.viewmodel.PaymentGatewayFactory
import com.razorpay.PaymentResultListener

class PaymentGateway : AppCompatActivity(), PaymentResultListener {
    private lateinit var binding: ActivityPaymentGatewayBinding
    private lateinit var paymentGatewayViewModel: PaymentGateWayeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_gateway)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_gateway)
        supportActionBar?.hide()
        paymentGatewayViewModel = ViewModelProvider(this, PaymentGatewayFactory(this))[PaymentGateWayeViewModel::class.java]
        binding.paymentGatewayVM = paymentGatewayViewModel
    }

    override fun onPaymentSuccess(s: String?) {
        // this method is called on payment success.
        paymentGatewayViewModel.callOrderUpdateAPI("Success")
        showDialog("Transaction Complete")
        Toast.makeText(applicationContext, "Payment is successful : " + s, Toast.LENGTH_SHORT).show();
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        paymentGatewayViewModel.callOrderUpdateAPI( "Failed")
        showDialog("Transaction Failed")
        Toast.makeText(applicationContext, "Payment Failed due to error : " +p1, Toast.LENGTH_SHORT)
            .show();
    }

    private fun showDialog(s: String) {
        SweetAlertDialog(applicationContext)
            .setTitleText(s)
            .setContentText("")
            .setConfirmText("Ok")
            .setConfirmClickListener {sdialog ->
                startActivity(Intent(applicationContext, HomeScreen::class.java))
            }
            .show()
    }

}