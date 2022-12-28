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

    fun btnSubmit() {
//        ShowSubmitDialog(
//            context, R.string.emailUpdate.toString(),
//            R.string.forgot_password_succesfully.toString()
//        )
        forgotModel = ForgotModel("", "", email.get().toString().trim())
        viewModelScope.launch {
            //Data store in model
            var status = 0

            CoroutineScope(Dispatchers.IO).launch {
                status = ForgotRepository.forgot(context, forgotModel)
                if (status == 1) {
                    context.startActivity(Intent(context, LoginScreen::class.java))
                } else {
                    Handler(Looper.getMainLooper()).post {
 //                       SweetDialog.dismissDialog()
                        Toast.makeText(context, "Invalid credential...", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


        fun ShowSubmitDialog(context: Context?, infoTitle: String?, message: String?) {
            submit_alert = Dialog(
                context!!
            )
            submit_alert?.setContentView(R.layout.submit_alert)
            submit_alert?.getWindow()!!.setBackgroundDrawable(
                ColorDrawable(
                    Color.WHITE
                )
            )
            val params: WindowManager.LayoutParams =
                submit_alert?.getWindow()!!.getAttributes()
            params.gravity = Gravity.CENTER or Gravity.CENTER_HORIZONTAL


            val btnOk =
                submit_alert?.findViewById<View>(
                    R.id.btnOk
                ) as Button


            btnOk.setOnClickListener {
                //TO DO
                submit_alert?.dismiss()

            }
            submit_alert?.show()
            submit_alert?.setCanceledOnTouchOutside(
                false
            )
        }

}


