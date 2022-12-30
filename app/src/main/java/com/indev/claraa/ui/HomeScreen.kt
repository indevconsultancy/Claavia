package com.indev.claraa.ui
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import cn.pedant.SweetAlert.SweetAlertDialog
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R
import com.indev.claraa.databinding.ActivityHomeScreenBinding
import com.indev.claraa.entities.CartModel
import com.indev.claraa.fragment.*
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.ProductRepository
import com.indev.claraa.util.GPSUtils
import android.Manifest
import android.location.Location
import android.location.LocationListener
import android.widget.Toast


class HomeScreen : AppCompatActivity(), LocationListener{
    private lateinit var binding: ActivityHomeScreenBinding
    lateinit var preferences: PrefHelper
    lateinit var cartArrayList: LiveData<List<CartModel>>
    private val TAG = "HOME"
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    var lat = ""
    var long = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen)
        supportActionBar?.hide()
        replaceFregment(Home(), 0)


        var badge= binding.bottomNavigation.bottomNavigation.getOrCreateBadge(R.id.order)
        cartArrayList = ProductRepository.getCartList(applicationContext)!!

         cartArrayList.observe(this) {
             badge.number = it.size
         }


        preferences= PrefHelper(this)
        binding.bottomNavigation.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    replaceFregment(Home(), 0)
                }
                R.id.profile -> {
                    replaceFregment(UserProfile(), 1)
                }
                R.id.order -> {
                    preferences.put(Constant.PREF_IS_CHECK_CART, false)
                    replaceFregment(Cart(),2)
                }

                R.id.refer -> {
                    replaceFregment(Refer(),3)
                }
                R.id.orderHistory -> {
                    replaceFregment(OrderHistory(),4)
                }
            }
            true
        }

        GPSUtils(this).turnOnGPS()
        getLocation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.GPS_CODE) {
                Log.d(TAG, "onActivityResult: SUCCESS")
//                Toast.makeText(applicationContext," gpsss on", Toast.LENGTH_LONG).show()
            } else {
                GPSUtils(this).turnOnGPS()
            }
        } else {
            GPSUtils(this).turnOnGPS()
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }




    override fun onBackPressed() {

        if(binding.bottomNavigation.bottomNavigation.selectedItemId == R.id.home) {
            super.onBackPressed()
            finishAffinity()

//            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText("Exit")
//                .setContentText("Are you sure to want to Exit?").setCancelText("Cancel")
//                .setConfirmText("Ok")
//                .setConfirmClickListener {
//                    finishAffinity()
//                }
//                .showCancelButton(true)
//                .setCancelClickListener { sDialog -> // Showing simple toast message to user
//                    sDialog.cancel()
//                }.show()
        }else{
           replaceFregment(Home(),1)
        }
    }


    private fun replaceFregment(fragment : Fragment, flag: Int) {
        val fragmentManager = supportFragmentManager
        val fragmentTransition= fragmentManager.beginTransaction()
//
        if(flag == 0){
            fragmentTransition.add(R.id.frame_layout, fragment)
            fragmentManager.popBackStack("FRAGMENT_1", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            fragmentTransition.addToBackStack("FRAGMENT_1")
        }else{
            fragmentTransition.replace(R.id.frame_layout, fragment)
            fragmentTransition.addToBackStack(null)
        }
        fragmentTransition.commit()
    }

    override fun onLocationChanged(location: Location) {
        lat= location.latitude.toString()
        long= location.longitude.toString()
        preferences.put("latitude",lat)
        preferences.put("longitude",long)
        Toast.makeText(applicationContext,"nckmfv" +lat + " "+ long,Toast.LENGTH_SHORT).show()
        
    }


}




