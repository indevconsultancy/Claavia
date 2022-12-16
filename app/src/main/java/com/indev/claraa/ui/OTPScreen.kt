package com.indev.claraa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.indev.claraa.R
import com.indev.claraa.databinding.ActivityMobileNumberBinding
import com.indev.claraa.databinding.ActivityOtpscreenBinding
import com.indev.claraa.viewmodel.MobileNumberViewModel
import com.indev.claraa.viewmodel.MobileNumberViewModelFactory
import com.indev.claraa.viewmodel.OTPViewModel
import com.indev.claraa.viewmodel.OTPViewModelFactory

class OTPScreen : AppCompatActivity() {
    private lateinit var binding: ActivityOtpscreenBinding
    private lateinit var otpViewModel: OTPViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_otpscreen)
        supportActionBar?.hide()

        otpViewModel = ViewModelProvider(this, OTPViewModelFactory(this))[OTPViewModel::class.java]
        binding.otpVM = otpViewModel
    }
}