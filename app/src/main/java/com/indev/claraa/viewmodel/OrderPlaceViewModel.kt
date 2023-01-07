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
        return ProductRepository.getCartList(context)
    }

    fun btnPlace(){
        ShowSubmitDialog()
      // context.startActivity(Intent(context, PaymentGateway::class.java))
    }

    fun ShowSubmitDialog() {
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


        val btnMale =
            submit_alert!!.findViewById<View>(
                R.id.btnMale
            ) as TextView

        btnMale.setOnClickListener {
            //TO DO
            submit_alert!!.dismiss()

        }
        submit_alert!!.show()
        submit_alert!!.setCanceledOnTouchOutside(
            false
        )
    }

    private fun showDialog() {
        SweetAlertDialog(context)
            .setTitleText("Make payment with")
            .setContentText("Credit")
            .setConfirmText("UPI")
            .setConfirmClickListener {sdialog ->
                sdialog.dismiss()
            }
            .show()
    }

}