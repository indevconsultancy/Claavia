package com.indev.claraa.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.indev.claraa.R
import com.indev.claraa.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val splashPreLength = 2000
    var splashLoaded = "No"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)

        supportActionBar?.hide()
        callNextActivity()
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