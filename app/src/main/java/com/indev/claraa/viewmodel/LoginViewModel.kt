package com.indev.claraa.viewmodel

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.indev.claraa.R
import com.indev.claraa.entities.LoginModel
import com.indev.claraa.fragment.Home
import com.indev.claraa.fragment.OTPScreen
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
    lateinit var progressDialog: ProgressDialog
    lateinit var loginModel: LoginModel
    lateinit var prefHelper: PrefHelper

    fun signIn(){
        progressDialog = ProgressDialog(context)
        SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
            .setContentText("Please Wait")
            .setConfirmText("Ok")
            .setConfirmClickListener {
                context.startActivity(Intent(context, Home::class.java))
            }
            .showCancelButton(true)
            .setCancelClickListener { sDialog -> // Showing simple toast message to user
                sDialog.cancel()
            }.show()
            nextActivity()
    }
    private fun nextActivity() {

        loginModel= LoginModel(username.get().toString().trim(),password.get().toString().trim())

        viewModelScope.launch {
            //Data store in model
            var status  =0

            CoroutineScope(Dispatchers.IO).launch {
                status  = LoginRepository.login(context,loginModel)
                progressDialog.dismiss()
                if (status== 1) {
                    prefHelper= PrefHelper(context)
                    prefHelper.put( Constant.PREF_IS_LOGIN,true)

                    context.startActivity(Intent(context, HomeScreen::class.java))
                } else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Invalid user", Toast.LENGTH_LONG).show()
                        progressDialog.dismiss()
                    }
                }
            }
        }
    }

        fun registration(){
        prefHelper= PrefHelper(context)
        prefHelper.put( Constant.PREF_IS_LOGIN,false)
        context.startActivity(Intent(context, UserRegistration::class.java))
    }

    fun forgot(){
        context.startActivity(Intent(context, EmailVarification::class.java))
    }

    fun otpVerify(){
        replaceFregment(OTPScreen())
//        context.startActivity(Intent(context, OTPScreen::class.java))
    }

    private fun replaceFregment(fragment : Fragment) {
        var transaction = (context as LoginScreen).supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }


}