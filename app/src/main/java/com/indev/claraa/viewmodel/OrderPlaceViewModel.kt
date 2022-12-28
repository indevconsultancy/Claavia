package com.indev.claraa.viewmodel

import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import cn.pedant.SweetAlert.SweetAlertDialog
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

    init {
        readAllData = OrderPlaceRepository.getAddress(context, 0)!!
    }

    fun getCartList(context: Context): LiveData<List<CartModel>>? {
        return ProductRepository.getCartList(context)
    }

    fun btnPlace(){

        context.startActivity(Intent(context, PaymentGateway::class.java))
    }

    private fun showDialog() {
        SweetAlertDialog(context)
            .setTitleText("Transaction Complete")
            .setContentText("")
            .setConfirmText("Ok")
            .setConfirmClickListener {sdialog ->
//                replaceFregment(Home())
                sdialog.dismiss()
            }
            .show()
    }

//    private fun replaceFregment(fragment : Fragment) {
//        var transaction = (context as HomeScreen).supportFragmentManager.beginTransaction()
//        transaction?.replace(R.id.frame_layout, fragment)
//        transaction.commit()
//    }
}