package com.indev.claraa.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.indev.claraa.R
import com.indev.claraa.databinding.FragmentOrderHistoryBinding
import com.indev.claraa.viewmodel.HomeScreenViewModel
import com.indev.claraa.viewmodel.HomeScreenViewModelFactory
import com.indev.claraa.viewmodel.OrderHistoryFactory
import com.indev.claraa.viewmodel.OrderHistoryViewModel

class OrderHistory : Fragment() {
    private lateinit var binding: FragmentOrderHistoryBinding
    private lateinit var orderHistoryViewModel: OrderHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_history, container, false)
        orderHistoryViewModel = ViewModelProvider(this,
            OrderHistoryFactory(requireContext())
        )[OrderHistoryViewModel::class.java]
        binding.orderHistoryVM = orderHistoryViewModel
        return binding.root
        // Inflate the layout for this fragment
    }
}