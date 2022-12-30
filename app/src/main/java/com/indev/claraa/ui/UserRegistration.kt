package com.indev.claraa.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.indev.claraa.R
import com.indev.claraa.databinding.ActivityUserRegistrationBinding
import com.indev.claraa.entities.DistrictModel
import com.indev.claraa.entities.StateModel
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.viewmodel.RegistrationViewModel
import com.indev.claraa.viewmodel.RegistrationViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserRegistration : AppCompatActivity() {
    private lateinit var binding: ActivityUserRegistrationBinding
    lateinit var registrationViewModel: RegistrationViewModel
    lateinit var preferences: PrefHelper
    var checkLogin: Boolean = false
    var stateOfUser: String? = null
    private lateinit var stateArrayList: ArrayList<StateModel>
    private lateinit var districtArrayList: ArrayList<DistrictModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_registration)
        supportActionBar?.hide()

        registrationViewModel = ViewModelProvider(
            this,
            RegistrationViewModelFactory(this)
        )[RegistrationViewModel::class.java]
        binding.registrationVM = registrationViewModel



        binding.btnMale.setOnClickListener {
            gender="Male"
            binding.btnMale.setBackgroundResource(R.drawable.selected_btn)
            binding.btnFemale.setBackgroundResource(R.drawable.btn_color_change)
            binding.btnOther.setBackgroundResource(R.drawable.btn_color_change)

        }
        binding.btnFemale.setOnClickListener {
            gender="Female"
            binding.btnMale.setBackgroundResource(R.drawable.btn_color_change)
            binding.btnFemale.setBackgroundResource(R.drawable.selected_btn)
            binding.btnOther.setBackgroundResource(R.drawable.btn_color_change)
        }

        binding.btnOther.setOnClickListener {
            gender="Others"
            binding.btnFemale.setBackgroundResource(R.drawable.btn_color_change)
            binding.btnMale.setBackgroundResource(R.drawable.btn_color_change)
            binding.btnOther.setBackgroundResource(R.drawable.selected_btn)

        }
        preferences= PrefHelper(this)
        checkLogin = preferences.getBoolean(Constant.PREF_IS_LOGIN)

        if(checkLogin ==true) {
            binding.llRegistrationWitheUs.visibility = View.GONE
            binding.llUpdateProfile.visibility = View.VISIBLE
            binding.llBottomImage.visibility = View.GONE
            binding.llPassword.visibility = View.GONE
            registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
            registrationViewModel.readAllData.observe(this, Observer {
                binding.btnSubmit.text = "Update"
                binding.etShopName.setText(it.shop_name)
                binding.etOwnerName.setText(it.owner_name)
                binding.etEmail.setText(it.email)
                binding.etMobile.setText(it.mobile_number)
                state_id=it.state_id.toInt()
                district_id =it.district_id.toInt()
                setStateSpinner(state_id)
                setDistrictSpinner( state_id,district_id)
                binding.etAddress.setText(it.address)
                binding.etPincode.setText(it.pinCode)
            })
            preferences.put(Constant.PREF_IS_UPDATE,true)
        }else{
            setStateSpinner(0)
        }



    }

    private fun setStateSpinner(stateId: Int) {
        stateArrayList = ArrayList<StateModel>()
        CoroutineScope(Dispatchers.IO).launch {
            stateArrayList = registrationViewModel.getStateList(applicationContext) as ArrayList<StateModel>
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
                    applicationContext,
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
                        state_id=id!!.toInt()
                        if(district_id==0) {
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
            districtArrayList = registrationViewModel.getDistrictList(applicationContext,state_id) as ArrayList<DistrictModel>
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
                    applicationContext,
                    android.R.layout.simple_spinner_item,
                    spinnerArray
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spnDistrict.setAdapter(adapter)
                if(district_id > 0){
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
        var gender=""
    }
}
