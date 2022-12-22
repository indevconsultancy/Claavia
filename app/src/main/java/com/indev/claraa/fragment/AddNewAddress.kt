package com.indev.claraa.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.indev.claraa.R
import com.indev.claraa.databinding.FragmentAddressDetailsBinding
import com.indev.claraa.entities.DistrictModel
import com.indev.claraa.entities.StateModel
import com.indev.claraa.ui.UserRegistration
import com.indev.claraa.viewmodel.AddressViewModel
import com.indev.claraa.viewmodel.AddressDetailsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNewAddress : Fragment() {
    private lateinit var binding: FragmentAddressDetailsBinding
    private lateinit var addressViewModel: AddressViewModel
    private lateinit var stateArrayList : ArrayList<StateModel>
    private lateinit var districtArrayList : ArrayList<DistrictModel>

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


        stateArrayList = ArrayList<StateModel>()
        CoroutineScope(Dispatchers.IO).launch {
            stateArrayList = context?.let { addressViewModel.getStateList(it) } as ArrayList<StateModel>
            val spinnerArray = arrayOfNulls<String>(stateArrayList.size)
            val spinnerMap = HashMap<Int, String>()
            for (i in 0 until stateArrayList.size) {
                spinnerMap[i] = stateArrayList.get(i).state_id
                spinnerArray[i] = stateArrayList.get(i).state_name
            }

            val adapter =
                context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, spinnerArray) }
            adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spnState.setAdapter(adapter)
            binding.spnState.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    var id = spinnerMap[binding.spnState.getSelectedItemPosition()]
                    Log.d("TAG", "onItemSelected: " + id)
                    UserRegistration.state_id = id!!.toInt()

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            })
        }

        districtArrayList = ArrayList<DistrictModel>()
        CoroutineScope(Dispatchers.IO).launch {
            districtArrayList = context?.let { addressViewModel.getDistrictList(it) } as ArrayList<DistrictModel>
            val spinnerArray = arrayOfNulls<String>(districtArrayList.size)
            val spinnerMap = HashMap<Int, String>()
            for (i in 0 until districtArrayList.size) {
                spinnerMap[i] = districtArrayList.get(i).district_id
                spinnerArray[i] = districtArrayList.get(i).district_name
            }

            val adapter =
                context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, spinnerArray) }
            adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spnDistrict.setAdapter(adapter)
            binding.spnDistrict.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    var id = spinnerMap[binding.spnDistrict.getSelectedItemPosition()]
                    Log.d("TAG", "onItemSelected: " + id)
                    UserRegistration.district_id = id!!.toInt()

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            })
        }


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

    companion object{
        var state_id=0
        var district_id=0
    }
}