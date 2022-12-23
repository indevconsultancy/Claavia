package com.indev.claraa.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.indev.claraa.SweetDialog
import com.indev.claraa.entities.LoginModel
import com.indev.claraa.fragment.Home
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.LoginRepository
import com.indev.claraa.ui.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(val context: Context): ViewModel() {

    var username: ObservableField<String> = ObservableField("")
    var password: ObservableField<String> = ObservableField("")
    lateinit var loginModel: LoginModel
    lateinit var prefHelper: PrefHelper
    var checkProfileUpdate= false

    fun signIn() {
        if (checkValidation()) {
            nextActivity()
        }
    }

    private fun nextActivity() {
        SweetDialog.showProgressDialog(context)
        loginModel= LoginModel(username.get().toString().trim(),password.get().toString().trim())
        prefHelper= PrefHelper(context)
        viewModelScope.launch {
            //Data store in model
            var status  =0

            CoroutineScope(Dispatchers.IO).launch {
                status  = LoginRepository.login(context,loginModel)
                if (status== 1) {
                    prefHelper.put(Constant.PREF_IS_LOGIN,true)
                    checkProfileUpdate = prefHelper.getBoolean(Constant.PREF_IS_UPDATE)
                    if(checkProfileUpdate == true){
                        context.startActivity(Intent(context, HomeScreen::class.java))
                    }else {
                        context.startActivity(Intent(context, UserRegistration::class.java))
                    }
                    SweetDialog.dismissDialog()
                } else {
                    Handler(Looper.getMainLooper()).post {
                        SweetDialog.dismissDialog()
                        Toast.makeText(context, "Invalid credential...", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun showDialog() {

        SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
            .setContentText("Please Wait")
            .setConfirmText("Ok")
            .setConfirmClickListener { sDialog ->

                sDialog.dismiss()
            }
            .setCancelClickListener { sDialog -> // Showing simple toast message to user
                sDialog.dismiss()
            }.show()
    }



    fun registration(){
        prefHelper= PrefHelper(context)
        prefHelper.put( Constant.PREF_IS_LOGIN,false)
        context.startActivity(Intent(context, UserRegistration::class.java))
    }

    fun forgot(){
        context.startActivity(Intent(context, EmailVarification::class.java))
    }

//    fun otpVerify(){
//        context.startActivity(Intent(context, MobileNumber::class.java))
//    }

    private fun checkValidation(): Boolean {
        if(username.get().toString().length<4) {
            Toast.makeText(context, "Please enter username..", Toast.LENGTH_SHORT).show()
            return false
        }

        if(password.get().toString().length<6) {
            Toast.makeText(context, "Please enter username..", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


}