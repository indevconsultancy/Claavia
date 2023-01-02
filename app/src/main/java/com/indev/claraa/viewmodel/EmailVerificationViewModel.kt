package com.indev.claraa.viewmodel

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.indev.claraa.R
import com.indev.claraa.SweetDialog
import com.indev.claraa.entities.ForgotModel
import com.indev.claraa.entities.LoginModel
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.ForgotRepository
import com.indev.claraa.repository.LoginRepository
import com.indev.claraa.ui.HomeScreen
import com.indev.claraa.ui.LoginScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmailVerificationViewModel  (val context: Context): ViewModel() {
    var email: ObservableField<String> = ObservableField("")
    var submit_alert: Dialog? = null
    lateinit var forgotModel: ForgotModel
    lateinit var prefHelper: PrefHelper

    fun btnSubmit() {
            SweetDialog.showProgressDialog(context)

            CoroutineScope(Dispatchers.IO).launch {
                forgotModel = ForgotModel(email.get().toString().trim())
                var status = ForgotRepository.forgot(context, forgotModel)
                if (status == 1) {
                    Handler(Looper.getMainLooper()).post {
                        showAlertDialog()
                    }
                } else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Invalid email address..", Toast.LENGTH_LONG).show()
                    }
                }
                SweetDialog.dismissDialog()
        }
    }


    private fun showAlertDialog() {
        SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
            .setContentText("Password has been sent on your registered email!")
            .setConfirmText("Ok")
            .setConfirmClickListener {sdialog ->
                context.startActivity(Intent(context, LoginScreen::class.java))
                sdialog.dismiss()
            }
            .show()
    }
}


