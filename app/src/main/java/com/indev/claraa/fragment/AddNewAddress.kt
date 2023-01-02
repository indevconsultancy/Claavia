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
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.ui.UserRegistration
import com.indev.claraa.viewmodel.AddressViewModel
import com.indev.claraa.viewmodel.AddressDetailsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddNewAddress : Fragment() {
    private lateinit var binding: FragmentAddressDetailsBinding
    private lateinit var addressViewModel: AddressViewModel
    private lateinit var stateArrayList : ArrayList<StateModel>
    private lateinit var districtArrayList : ArrayList<DistrictModel>
    lateinit var preferences: PrefHelper
    var check_new_address: Boolean = false
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

        preferences= PrefHelper(requireContext())
        check_new_address = preferences.getBoolean(Constant.PREF_NEWADDRESS)

        if(check_new_address ==false) {
        addressViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                binding.etShopName.setText(it.shop_name)
                binding.etMobile.setText(it.mobile_number)
                binding.etUserName.setText(it.user_name)
                binding.etAddress1.setText(it.address1)
                binding.etAddress2.setText(it.address2)
                state_id =it.state_id.toInt()
                district_id =it.district_id.toInt()
                setStateSpinner(state_id)
                setDistrictSpinner(state_id, district_id)
                binding.etLandmark.setText(it.landmark)
                binding.etPinCode.setText(it.pinCode)

            }
        })
        }else {
            state_id=0
            district_id=0
            setStateSpinner(0)
        }

    }

    private fun replaceFregment(fragment : Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransition= fragmentManager?.beginTransaction()
        fragmentTransition?.replace(R.id.frame_layout, fragment)
        fragmentTransition?.addToBackStack(null)
        fragmentTransition?.commit()
    }

    private fun setStateSpinner(stateId: Int) {
        stateArrayList = ArrayList<StateModel>()
        CoroutineScope(Dispatchers.IO).launch {
            stateArrayList = addressViewModel.getStateList(requireContext()) as ArrayList<StateModel>
            var stateModel= StateModel("0","Select State","0")
            stateArrayList.add(0, stateModel)
            val spinnerArray = arrayOfNulls<String>(stateArrayList.size)
            val spinnerMap = HashMap<Int, String>()
            for (i in 0 until stateArrayList.size) {
                spinnerMap[i] = stateArrayList.get(i).state_id
                spinnerArray[i] = stateArrayList.get(i).state_name
            }
            withContext(Dispatchers.Main) {

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    spinnerArray
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spnState.setAdapter(adapter)
                if(state_id > 0){
                    var strStateName=""
                    for(i in stateArrayList){
                        if(i.state_id.equals(state_id.toString())) {
                            strStateName=  i.state_name
                        }
                        var position= adapter.getPosition(strStateName)
                        binding.spnState.setSelection(position)
                    }
                }
                binding.spnState.setOnItemSelectedListener(object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        var id = spinnerMap[binding.spnState.getSelectedItemPosition()]
                        state_id =id!!.toInt()
                        if(district_id ==0) {
                            setDistrictSpinner(state_id, 0)
                        }

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                })
            }
        }
    }

    private fun setDistrictSpinner(state_id: Int,  districtId: Int) {
        districtArrayList = ArrayList<DistrictModel>()
        CoroutineScope(Dispatchers.IO).launch {
            districtArrayList = addressViewModel.getDistrictList(requireContext(),state_id) as ArrayList<DistrictModel>
            var da= DistrictModel("0","Select District","0","0")
            districtArrayList.add(0, da)
            val spinnerArray = arrayOfNulls<String>(districtArrayList.size)
            val spinnerMap = HashMap<Int, String>()

            for (i in 0 until districtArrayList.size) {

                spinnerMap[i] = districtArrayList.get(i).district_id
                spinnerArray[i] = districtArrayList.get(i).district_name
            }
            withContext(Dispatchers.Main) {

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    spinnerArray
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spnDistrict.setAdapter(adapter)
                if(UserRegistration.district_id > 0){
                    var strDistrictName=""
                    for(i in districtArrayList){
                        if(i.district_id.equals(districtId.toString())) {
                            strDistrictName=  i.district_name
                        }
                        var position= adapter.getPosition(strDistrictName)
                        binding.spnDistrict.setSelection(position)
                    }
                }
                binding.spnDistrict.setOnItemSelectedListener(object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        var id = spinnerMap[binding.spnDistrict.getSelectedItemPosition()]
                        Log.d("TAG", "onItemSelected: " + id)
                        district_id = id!!.toInt()

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                })
            }
        }
    }

    companion object{
        var state_id=0
        var district_id=0
    }
}