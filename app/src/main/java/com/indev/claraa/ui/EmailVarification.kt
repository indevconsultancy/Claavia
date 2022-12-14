package com.indev.claraa.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.indev.claraa.R
import com.indev.claraa.databinding.ActivityEmailVarificationBinding
import com.indev.claraa.viewmodel.EmailVerificationViewModel
import com.indev.claraa.viewmodel.EmailVerificationViewModelFactory

class EmailVarification : AppCompatActivity() {
    private lateinit var binding: ActivityEmailVarificationBinding
    private lateinit var emailVerificationViewModel: EmailVerificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,  R.layout.activity_email_varification)

        supportActionBar?.hide()

        emailVerificationViewModel = ViewModelProvider(this, EmailVerificationViewModelFactory(this))[EmailVerificationViewModel::class.java]
        binding.emailVM = emailVerificationViewModel
    }

}