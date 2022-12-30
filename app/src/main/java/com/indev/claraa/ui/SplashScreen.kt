package com.indev.claraa.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.indev.claraa.R
import com.indev.claraa.databinding.ActivitySplashScreenBinding
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.viewmodel.SplashViewModel
import com.indev.claraa.viewmodel.SplashViewModelFactory

class SplashScreen : AppCompatActivity(), LocationListener {
    private lateinit var binding: ActivitySplashScreenBinding
    private val splashPreLength = 8000
    var splashLoaded = "No"
    lateinit var splashViewModel: SplashViewModel
    lateinit var prefHelper: PrefHelper
    var checkLogin: Boolean = false
    private val REQUEST = 112
    var ret = false
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    var lat = ""
    var long = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        supportActionBar?.hide()
        prefHelper = PrefHelper(applicationContext)

        val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS,
                REQUEST
            )
        }else{
            callNextActivity()
        }

        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        splashViewModel = ViewModelProvider(this, SplashViewModelFactory(this@SplashScreen))[SplashViewModel::class.java]
    }

    override fun onLocationChanged(location: Location) {
        lat= location.latitude.toString()
        long= location.longitude.toString()
        prefHelper.put(Constant.PREF_LATITUDE,lat)
        prefHelper.put(Constant.PREF_LONGITUDE,long)
    }
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG", "@@@ PERMISSIONS grant")
                    callNextActivity()
                } else {
                    Log.d("TAG", "@@@ PERMISSIONS Denied")
                    Toast.makeText(this, "PERMISSIONS Denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun hasPermissions(context: Context?, permissions: Array<String>?): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    private fun callNextActivity() {
        getLocation()
        checkLogin = prefHelper.getBoolean(Constant.PREF_IS_LOGIN)
        Handler().postDelayed({
            if(checkLogin == true) {
                val intent = Intent(this@SplashScreen, HomeScreen::class.java)
                startActivity(intent)
                finishAffinity()
            }else{
                prefHelper.put(Constant.PREF_SPLASH, true)
                val intent = Intent(this@SplashScreen, IntroScreen::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }, splashPreLength.toLong())
    }


}