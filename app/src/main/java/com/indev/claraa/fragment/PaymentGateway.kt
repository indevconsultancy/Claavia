package com.indev.claraa.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.indev.claraa.R
import com.indev.claraa.databinding.FragmentOrderPlaceBinding
import com.indev.claraa.databinding.FragmentPaymentGatewayBinding
import com.indev.claraa.viewmodel.*

class PaymentGateway : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentPaymentGatewayBinding
     lateinit var paymentGateWayeViewModel: PaymentGateWayeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_payment_gateway, container, false)
        paymentGateWayeViewModel = ViewModelProvider(
            this,
            PaymentGatewayFactory(requireContext())
        )[PaymentGateWayeViewModel::class.java]
        binding.paymentGatewayVM = paymentGateWayeViewModel
        return binding.root
        return inflater.inflate(R.layout.fragment_payment_gateway, container, false)
    }


}