package com.indev.claraa.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.indev.claraa.R
import com.indev.claraa.databinding.ActivitySplashScreenBinding
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.SplashRepository
import com.indev.claraa.restApi.ClaraaApi
import com.indev.claraa.restApi.ClientApi
import com.indev.claraa.roomdb.RoomDB
import com.indev.claraa.viewmodel.SplashViewModel
import com.indev.claraa.viewmodel.SplashViewModelFactory

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val splashPreLength = 2000
    var splashLoaded = "No"
    lateinit var splashViewModel: SplashViewModel
    lateinit var prefHelper: PrefHelper
    var checkLogin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)

        supportActionBar?.hide()
        prefHelper = PrefHelper(applicationContext)

        callNextActivity()
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        splashViewModel = ViewModelProvider(this, SplashViewModelFactory(this@SplashScreen))[SplashViewModel::class.java]

    }

    private fun callNextActivity() {
        checkLogin = prefHelper.getBoolean(Constant.PREF_IS_LOGIN)

        Handler().postDelayed({
            if(checkLogin == true) {
                    val intent = Intent(this@SplashScreen, HomeScreen::class.java)
                    startActivity(intent)
                    finishAffinity()
            }else{
                val intent = Intent(this@SplashScreen, LoginScreen::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }, splashPreLength.toLong())
    }


}