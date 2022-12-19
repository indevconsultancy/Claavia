package com.indev.claraa

import android.content.Context
import android.graphics.Color
import cn.pedant.SweetAlert.SweetAlertDialog

class SweetDialog {

    companion object {
        var sweetAlertDialog: SweetAlertDialog? = null
        fun showProgressDialog(context: Context?) {
            sweetAlertDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
            sweetAlertDialog!!.progressHelper.barColor = Color.parseColor("#006E89")
            sweetAlertDialog!!.titleText = "Please wait..."
            sweetAlertDialog!!.setCancelable(false)
            sweetAlertDialog!!.show()
        }

        fun dismissDialog() {
            sweetAlertDialog!!.dismiss()
        }
    }
}