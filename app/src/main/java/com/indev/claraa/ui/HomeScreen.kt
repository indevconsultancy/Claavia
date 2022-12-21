package com.indev.claraa.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import cn.pedant.SweetAlert.SweetAlertDialog
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R
import com.indev.claraa.adapter.CartAdapter
import com.indev.claraa.databinding.ActivityHomeScreenBinding
import com.indev.claraa.entities.CartModel
import com.indev.claraa.fragment.*
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.ProductRepository
import com.indev.claraa.viewmodel.ProductDetailViewModel

class HomeScreen : AppCompatActivity(), ClickLinstener {
    private lateinit var binding: ActivityHomeScreenBinding
    lateinit var preferences: PrefHelper
    lateinit var cartArrayList: LiveData<List<CartModel>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen)
        supportActionBar?.hide()
        replaceFregment(Home(), 0)
        var badge= binding.bottomNavigation.bottomNavigation.getOrCreateBadge(R.id.order)
        cartArrayList = ProductRepository.getCartList(applicationContext)!!

         cartArrayList.observe(this,{
             badge.number =it.size
        })
        preferences= PrefHelper(this)
        binding.bottomNavigation.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    replaceFregment(Home(), 0)
                }
                R.id.profile -> {
                    replaceFregment(Profile(), 1)
                }
                R.id.order -> {
                    preferences.put(Constant.PREF_IS_CHECK_CART, false)
                    replaceFregment(Cart(),2)
                }

                R.id.refer -> {
                    replaceFregment(Refer(),3)
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
           // replaceFregment(Home(),1)
        }
    }


    private fun replaceFregment(fragment : Fragment, flag: Int) {
        val fragmentManager = supportFragmentManager
        val fragmentTransition= fragmentManager.beginTransaction()

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

    override fun onClickListner(position: Int) {
    }

    override fun updateTextView(amount: Int) {
    }

    override fun updatePowerRange(power_range: String) {
    }

}




