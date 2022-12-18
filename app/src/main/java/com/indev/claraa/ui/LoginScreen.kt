package com.indev.claraa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.indev.claraa.R
import com.indev.claraa.databinding.LoginScreenBinding
import com.indev.claraa.fragment.AddressList
import com.indev.claraa.viewmodel.LoginViewModel
import com.indev.claraa.viewmodel.LoginViewModelFactory

class LoginScreen : AppCompatActivity() {
    private lateinit var binding: LoginScreenBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.login_screen)
        supportActionBar?.hide()

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(this))[LoginViewModel::class.java]
        binding.loginVM = loginViewModel

    }
}