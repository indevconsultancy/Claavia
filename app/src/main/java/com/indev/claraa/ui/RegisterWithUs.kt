package com.indev.claraa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.indev.claraa.R
import com.indev.claraa.databinding.ActivityRegisterWithUsBinding
import com.indev.claraa.viewmodel.*

class RegisterWithUs : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterWithUsBinding
    private lateinit var registerWithUsViewModel: RegisterWithUsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register_with_us)
        supportActionBar?.hide()

        registerWithUsViewModel = ViewModelProvider(this, RegisterWithUsViewModelFactory(this))[RegisterWithUsViewModel::class.java]
        binding.registrationWithUsVM = registerWithUsViewModel
    }
}