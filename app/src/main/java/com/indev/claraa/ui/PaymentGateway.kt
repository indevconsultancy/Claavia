package com.indev.claraa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.indev.claraa.R
import com.indev.claraa.databinding.ActivityPaymentGatewayBinding
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
        binding.toolbar.toolbarTitle.text = "Payment Status"
    }

    override fun onPaymentSuccess(s: String?) {
        // this method is called on payment success.
        paymentGatewayViewModel.callOrderUpdateAPI("Success")
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        paymentGatewayViewModel.callOrderUpdateAPI( "Failed")
    }

}