package com.indev.claraa.fragment

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.indev.claraa.R
import com.indev.claraa.adapter.HomeAdapter
import com.indev.claraa.databinding.FragmentAddressDetailsBinding
import com.indev.claraa.databinding.FragmentHomeBinding
import com.indev.claraa.ui.HomeScreen
import com.indev.claraa.viewmodel.AddressDetailsViewModel
import com.indev.claraa.viewmodel.AddressDetailsViewModelFactory
import com.indev.claraa.viewmodel.HomeScreenViewModel
import com.indev.claraa.viewmodel.HomeScreenViewModelFactory

class AddressDetails : Fragment() {
    private lateinit var binding: FragmentAddressDetailsBinding
    private lateinit var addressDetailsViewModel: AddressDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_address_details, container, false)
        addressDetailsViewModel = ViewModelProvider(
            this,
            AddressDetailsViewModelFactory(requireContext())
        )[AddressDetailsViewModel::class.java]
        binding.addressDetailsVM = addressDetailsViewModel

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { _, keyCode, event ->
            event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.menuClick.setOnClickListener(){
            replaceFregment(AddressList())
        }

    }

    private fun replaceFregment(fragment : Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransition= fragmentManager?.beginTransaction()
        fragmentTransition?.replace(R.id.frame_layout, fragment)
        fragmentTransition?.commit()
    }




}