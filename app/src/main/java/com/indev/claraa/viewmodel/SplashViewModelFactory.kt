package com.indev.claraa.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.indev.claraa.repository.SplashRepository

class SplashViewModelFactory (private val context: Context): ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SplashViewModel(context) as T
    }
}