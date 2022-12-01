package com.indev.claraa.viewmodel

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.indev.claraa.R

class EmailVerificationViewModel  (val context: Context): ViewModel() {
    var email: ObservableField<String> = ObservableField("")
    var submit_alert: Dialog? = null

    fun btnSubmit() {
        ShowSubmitDialog(
            context, R.string.emailUpdate.toString(),
            R.string.forgot_password_succesfully.toString()
        )
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


