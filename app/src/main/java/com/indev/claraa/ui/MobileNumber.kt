package com.indev.claraa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.indev.claraa.R
import com.indev.claraa.databinding.ActivityMobileNumberBinding
import com.indev.claraa.viewmodel.LoginViewModel
import com.indev.claraa.viewmodel.LoginViewModelFactory
import com.indev.claraa.viewmodel.MobileNumberViewModel
import com.indev.claraa.viewmodel.MobileNumberViewModelFactory

class MobileNumber : AppCompatActivity() {
    private lateinit var binding: ActivityMobileNumberBinding
    private lateinit var mobileNumberViewModel: MobileNumberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_mobile_number)
        supportActionBar?.hide()

        mobileNumberViewModel = ViewModelProvider(this, MobileNumberViewModelFactory(this))[MobileNumberViewModel::class.java]
        binding.mobileNoVM = mobileNumberViewModel
    }
}