package com.indev.claraa.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R
import com.indev.claraa.adapter.CartAdapter
import com.indev.claraa.databinding.ActivityHomeScreenBinding
import com.indev.claraa.fragment.Cart
import com.indev.claraa.fragment.Home
import com.indev.claraa.fragment.Profile
import com.indev.claraa.fragment.Refer
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper

class HomeScreen : AppCompatActivity(), ClickLinstener {
    private lateinit var binding: ActivityHomeScreenBinding
    lateinit var preferences: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen)
        supportActionBar?.hide()
        replaceFregment(Home())
        var badge= binding.bottomNavigation.bottomNavigation.getOrCreateBadge(R.id.order)
        badge.number = CartAdapter.totalProduct
        preferences= PrefHelper(this)
        binding.bottomNavigation.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    replaceFregment(Home())
                }
                R.id.profile -> {
                    replaceFregment(Profile())
                }
                R.id.order -> {
                    preferences.put(Constant.PREF_IS_CHECK_CART, false)
                    replaceFregment(Cart())
                }

                R.id.refer -> {
                    replaceFregment(Refer())
                }
            }
            true
        }
    }


    override fun onBackPressed() {

        if(binding.bottomNavigation.bottomNavigation.selectedItemId == R.id.home) {
            super.onBackPressed()

            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText("Exit")
                .setContentText("Are you sure to want to Exit?").setCancelText("Cancel")
                .setConfirmText("Ok")
                .setConfirmClickListener {
                    finishAffinity()
                }
                .showCancelButton(true)
                .setCancelClickListener { sDialog -> // Showing simple toast message to user
                    sDialog.cancel()
                }.show()
        }else{
            binding.bottomNavigation.bottomNavigation.selectedItemId ==R.id.home
        }
    }


    private fun replaceFregment(fragment : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransition= fragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.frame_layout, fragment)
        fragmentTransition.addToBackStack(null)
        fragmentTransition.commit()
    }

    override fun onClickListner(position: Int) {
    }

    override fun updateTextView(amount: Int) {
    }

    override fun updatePowerRange(power_range: String) {
    }

}




