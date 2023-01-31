package com.indev.claraa.viewmodel

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import cn.pedant.SweetAlert.SweetAlertDialog
import com.indev.claraa.R
import com.indev.claraa.entities.AddressDetailsModel
import com.indev.claraa.entities.CartModel
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.OrderPlaceRepository
import com.indev.claraa.repository.ProductRepository
import com.indev.claraa.ui.PaymentGateway

class OrderPlaceViewModel (val context: Context): ViewModel() {
    var address: ObservableField<String> = ObservableField("")
    val readAllData: LiveData<AddressDetailsModel>
    var prefHelper: PrefHelper
    var submit_alert: Dialog? = null


    init {
        prefHelper = PrefHelper(context)
        readAllData = OrderPlaceRepository.getAddress(context,
            prefHelper.getString(Constant.PREF_ADDRESS_ID)!!
        )!!
    }

    fun getCartList(context: Context): LiveData<List<CartModel>>? {
        return ProductRepository.getCartDataList(context)
    }

    fun btnPlace(){
        ShowSubmitDialog()
    }

    fun ShowSubmitDialog() {
        var selectPaymentMode=""
        submit_alert = Dialog(
            context
        )
        submit_alert!!.setContentView(R.layout.custom_select_payment)
        submit_alert!!.getWindow()?.setBackgroundDrawable(
            ColorDrawable(
                Color.WHITE
            )
        )
        val params: WindowManager.LayoutParams =
            submit_alert!!.getWindow()!!.getAttributes()
        params.gravity = Gravity.CENTER or Gravity.CENTER_HORIZONTAL


        val btnCredit =
            submit_alert!!.findViewById<View>(
                R.id.btnCredit
            ) as TextView
        val btnPayment =
            submit_alert!!.findViewById<View>(
                R.id.btnPayment
            ) as TextView
        val btnBoth =
            submit_alert!!.findViewById<View>(
                R.id.btnBoth
            ) as TextView

        val tvCreditLimit =
            submit_alert!!.findViewById<View>(
                R.id.tvCreditLimit
            ) as TextView


        tvCreditLimit.text = "Available Credit Limit: " + prefHelper.getString(Constant.PREF_CREDIT)
        btnCredit.setOnClickListener {
            //TO DO
            selectPaymentMode = "Credit"
            prefHelper.put(Constant.PREF_PAYMENT_MODE,selectPaymentMode)
            context.startActivity(Intent(context, PaymentGateway::class.java))
            submit_alert!!.dismiss()
        }

        btnPayment.setOnClickListener {
            //TO DO
            selectPaymentMode= "Payment"
            prefHelper.put(Constant.PREF_PAYMENT_MODE,selectPaymentMode)
            context.startActivity(Intent(context, PaymentGateway::class.java))
            submit_alert!!.dismiss()
        }

        btnBoth.setOnClickListener {
            //TO DO
            selectPaymentMode= "Both"
            prefHelper.put(Constant.PREF_PAYMENT_MODE,selectPaymentMode)
            context.startActivity(Intent(context, PaymentGateway::class.java))
            submit_alert!!.dismiss()
        }


        submit_alert!!.show()
        submit_alert!!.setCanceledOnTouchOutside(
            false
        )
    }


}