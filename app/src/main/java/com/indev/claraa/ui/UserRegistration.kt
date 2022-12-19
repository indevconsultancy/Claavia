package com.indev.claraa.ui

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintSet.GONE
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
        supportActionBar?.hide()

        registrationViewModel = ViewModelProvider(
            this,
            RegistrationViewModelFactory(this)
        )[RegistrationViewModel::class.java]
        binding.registrationVM = registrationViewModel

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
}
