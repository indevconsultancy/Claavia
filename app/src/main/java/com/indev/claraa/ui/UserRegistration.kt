package com.indev.claraa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.indev.claraa.R
import com.indev.claraa.databinding.ActivityUserRegistrationBinding
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.viewmodel.RegistrationViewModel
import com.indev.claraa.viewmodel.RegistrationViewModelFactory

class UserRegistration : AppCompatActivity() {
    private lateinit var binding: ActivityUserRegistrationBinding
    lateinit var registrationViewModel: RegistrationViewModel
    lateinit var preferences: PrefHelper
    var checkLogin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_registration)
        title = "Registration"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        registrationViewModel = ViewModelProvider(
            this,
            RegistrationViewModelFactory(this)
        )[RegistrationViewModel::class.java]
        binding.registrationVM = registrationViewModel

        preferences= PrefHelper(this)
        checkLogin = preferences.getBoolean(Constant.PREF_IS_LOGIN)
        if(checkLogin ==true) {
            registrationViewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
            registrationViewModel.readAllData.observe(this, Observer {

                binding.btnSubmit.setText("Update")
                binding.etShopName.setText(it.shop_name)
                binding.etUserName.setText(it.user_name)
                binding.etEmail.setText(it.email)
                binding.etMobile.setText(it.mobile_number)
                binding.spnState.setSelection(1)
                binding.spnDistrict.setSelection(1)
                binding.etAddress.setText(it.address)
                binding.etPincode.setText(it.pinCode)
            })
        }
    }


}
