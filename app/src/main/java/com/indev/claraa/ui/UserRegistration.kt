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
import com.indev.claraa.entities.StateModel
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.viewmodel.RegistrationViewModel
import com.indev.claraa.viewmodel.RegistrationViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UserRegistration : AppCompatActivity() {
    private lateinit var binding: ActivityUserRegistrationBinding
    lateinit var registrationViewModel: RegistrationViewModel
    lateinit var preferences: PrefHelper
    var checkLogin: Boolean = false
    var stateOfUser: String? = null
    private lateinit var stateArrayList: ArrayList<StateModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_registration)
        supportActionBar?.hide()

        registrationViewModel = ViewModelProvider(
            this,
            RegistrationViewModelFactory(this)
        )[RegistrationViewModel::class.java]
        binding.registrationVM = registrationViewModel


        stateArrayList = ArrayList<StateModel>()
        CoroutineScope(Dispatchers.IO).launch {
            stateArrayList = registrationViewModel.getStateList(applicationContext) as ArrayList<StateModel>
            val spinnerArray = arrayOfNulls<String>(stateArrayList.size)
            val spinnerMap = HashMap<Int, String>()
            for (i in 0 until stateArrayList.size) {
                spinnerMap[i] = stateArrayList.get(i).state_id
                spinnerArray[i] = stateArrayList.get(i).state_name
            }

            val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, spinnerArray)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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
                    state_id= id!!.toInt()

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            })
        }

        preferences= PrefHelper(this)
        checkLogin = preferences.getBoolean(Constant.PREF_IS_LOGIN)
        if(checkLogin ==true) {
            binding.llRegistrationWitheUs.visibility = View.GONE
            binding.llUpdateProfile.visibility = View.VISIBLE
            binding.llBottomImage.visibility = View.GONE
            registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
            registrationViewModel.readAllData.observe(this, Observer {
                binding.btnSubmit.setText("Update")
                binding.etShopName.setText(it.shop_name)
                binding.etUserName.setText(it.user_name)
                binding.etOwnerName.setText(it.owner_name)
                binding.etEmail.setText(it.email)
                binding.etMobile.setText(it.mobile_number)
                binding.spnState.setSelection(1)
                binding.spnDistrict.setSelection(1)
                binding.etAddress.setText(it.address)
                binding.etPincode.setText(it.pinCode)
            })
            preferences.put(Constant.PREF_IS_UPDATE,true)
        }
    }

    companion object{
        var state_id=0
    }
}
