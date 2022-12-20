package com.indev.claraa.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.indev.claraa.R
import com.indev.claraa.databinding.FragmentAddressDetailsBinding
import com.indev.claraa.viewmodel.AddressViewModel
import com.indev.claraa.viewmodel.AddressDetailsViewModelFactory

class AddNewAddress : Fragment() {
    private lateinit var binding: FragmentAddressDetailsBinding
    private lateinit var addressViewModel: AddressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_address_details, container, false)
        addressViewModel = ViewModelProvider(
            this,
            AddressDetailsViewModelFactory(requireContext())
        )[AddressViewModel::class.java]
        binding.addressDetailsVM = addressViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.backClick.setOnClickListener(){
            replaceFregment(AddressList())
        }
        binding.toolbar.toolbarTitle.text = "Add New Address"
        addressViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                binding.etShopName.setText(it.shop_name)
                binding.etMobile.setText(it.mobile_number)
                binding.etUserName.setText(it.user_name)
                binding.etAddress1.setText(it.address1)
                binding.etAddress2.setText(it.address2)
                binding.spnState.setSelection(1)
                binding.spnDistrict.setSelection(1)
                binding.etLandmark.setText(it.landmark)
                binding.etPinCode.setText(it.pinCode)

            }
        })


    }

    private fun replaceFregment(fragment : Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransition= fragmentManager?.beginTransaction()
        fragmentTransition?.replace(R.id.frame_layout, fragment)
        fragmentTransition?.addToBackStack(null)
        fragmentTransition?.commit()
    }
}