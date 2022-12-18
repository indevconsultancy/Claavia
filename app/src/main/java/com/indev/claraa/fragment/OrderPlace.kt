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
import com.indev.claraa.databinding.FragmentProfileBinding
import com.indev.claraa.viewmodel.*

class OrderPlace : Fragment() {
    private lateinit var binding: FragmentOrderPlaceBinding
    private lateinit var orderPlaceViewModel: OrderPlaceViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_order_place, container, false)
        orderPlaceViewModel = ViewModelProvider(
            this,
            OrderPlaceViewModelFactory(requireContext())
        )[OrderPlaceViewModel::class.java]
        binding.orderPlaceVM = orderPlaceViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.toolbarTitle.text = "Order Place"
    }

}