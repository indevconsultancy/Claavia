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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)

        supportActionBar?.hide()
        callNextActivity()

        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val userDao = RoomDB.getDatabase(this@SplashScreen).userDao()
        val apiInterface = ClientApi.getClient()?.create(ClaraaApi::class.java)
        val splashRepo = SplashRepository(apiInterface,userDao)

        splashViewModel = ViewModelProvider(this, SplashViewModelFactory(this@SplashScreen, splashRepo))[SplashViewModel::class.java]


    }

    private fun callNextActivity() {
        Handler().postDelayed({

            if (splashLoaded == "No") {
                val intent = Intent(this@SplashScreen, LoginScreen::class.java)
                startActivity(intent)
                finishAffinity()

            }
        }, splashPreLength.toLong())
    }


}