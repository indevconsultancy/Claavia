package com.indev.claraa.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.ViewModel
import cn.pedant.SweetAlert.SweetAlertDialog
import com.indev.claraa.R
import com.indev.claraa.entities.CartModel
import com.indev.claraa.entities.OrderDetailsModel
import com.indev.claraa.entities.OrderMasterModel
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.PaymentGatewayRepository
import com.indev.claraa.roomdb.RoomDB
import com.indev.claraa.ui.HomeScreen
import com.indev.claraa.util.CommonClass
import com.razorpay.Checkout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class PaymentGateWayeViewModel (val context: Context): ViewModel() {

    lateinit var cartArrayList: ArrayList<CartModel>
    lateinit var orderDetailsArrayList: ArrayList<OrderDetailsModel>
    lateinit var orderMasterModel: OrderMasterModel
    lateinit var orderDetailsModel: OrderDetailsModel
    lateinit var prefHelper: PrefHelper
    private var dataBase: RoomDB? = null
    var last_order_master_id: Long =0
    var last_order_details_id: Long=0
    var user_id=0
    var totalAmount=0.0
    var last_id=0
    var creditValues=0.0
    var updatedCredit=0.0

    init {
        setOrderData()
        Checkout.preload(context)
    }

    private fun initializeDB(context: Context): RoomDB? {
        return RoomDB.getDatabase(context)
    }

    @SuppressLint("NewApi")
    private fun setOrderData() {
        prefHelper = PrefHelper(context)
        dataBase = initializeDB(context)
         user_id= prefHelper.getString(Constant.PREF_USERID)!!.toInt()
        CoroutineScope(Dispatchers.IO).launch {
            cartArrayList= PaymentGatewayRepository.getCartList(context) as ArrayList<CartModel>
            totalAmount= grandTotal(cartArrayList)
//            totalAmount= 1.0
            var orderMasterUniqueId= CommonClass.getUniqueId().toString()

            if(cartArrayList.size> 0) {
                orderMasterModel = OrderMasterModel(
                    0,
                    orderMasterUniqueId ,
                    user_id.toString(),
                    CommonClass.currentDate().toString(),
                    totalAmount.toString(),
                    "",
                    prefHelper.getString(Constant.PREF_ACTIVE).toString(),
                    "",
                    prefHelper.getString(Constant.PREF_LATITUDE).toString(),prefHelper.getString(Constant.PREF_LONGITUDE).toString(),
                    "Pending"
                )
                last_order_master_id= dataBase?.userDao()?.insertOrderMaster(orderMasterModel)!!
//                PaymentGatewayRepository.insertOrderMaster(context, orderMasterModel)
//                    PaymentGatewayRepository.insertOrderDetails(context, orderDetailsModel)
                callOrderAPI(totalAmount)

            }
        }
    }

    private fun grandTotal(size: List<CartModel>): Double {
        var totalPrice = 0.0
        for (i in size.indices) {
            totalPrice += size[i].amount
        }
        return totalPrice
    }

    fun payment(totalAmount: Double) {
        prefHelper = PrefHelper(context)
        var user_name= prefHelper.getString(Constant.PREF_USER_NAME)
        var email= prefHelper.getString(Constant.PREF_USER_EMAIL)
        var mobile_number= prefHelper.getString(Constant.PREF_USER_MOBILE)
        val amt = 1
        val amount = Math.round(amt.toFloat() * 100).toInt()
        val checkout = Checkout()
        checkout.setKeyID("rzp_live_dJgqeHwFE2LXNp")
        try {
            val obj = JSONObject()
            obj.put("name", user_name)
            obj.put("description", "Payment")
            obj.put("theme.color", "")
            obj.put("currency", "INR")
            obj.put("amount", totalAmount *100)
            obj.put("prefill.contact", mobile_number)
            obj.put("prefill.email", email)
            val retrObj = JSONObject()
            retrObj.put("enabled", true)
            retrObj.put("max_count", 10000)
            obj.put("retry", retrObj)
            checkout.open(context as Activity?, obj)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("SuspiciousIndentation", "NewApi")
    private fun callOrderAPI(amount: Double) {
        dataBase = initializeDB(context)
        CoroutineScope(Dispatchers.IO).launch {
                last_id = PaymentGatewayRepository.insertOrderMasterAPI(context,orderMasterModel)

                if (last_id> 0) {
                    PaymentGatewayRepository.updateOrderMasterbyId(last_id,last_order_master_id.toInt(), context)
                    for (i in cartArrayList) {
                        orderDetailsModel = OrderDetailsModel(
                            0,
                            CommonClass.getUniqueId().toString(),
                            last_id.toString(),
                            i.product_id, i.price.toInt(),
                            i.quantity.toInt(),
                            "Pending",
                            i.price.toInt() * i.quantity.toInt(),
                            user_id,prefHelper.getString(Constant.PREF_ACTIVE).toString()
                        )
                        last_order_details_id = dataBase?.userDao()?.insertOrderDetails(orderDetailsModel)!!
                        var  last_order_id = PaymentGatewayRepository.insertOrderDetailAPI(context, orderDetailsModel)
                        if (last_order_id> 0) {
                            PaymentGatewayRepository.updateOrderDetailsbyId(last_order_id,last_order_details_id.toInt(), context)
                        } else {
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(context, "Something went wrong...", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    prefHelper = PrefHelper(context)
                    var creditValue= prefHelper.getString(Constant.PREF_CREDIT)
                    if(creditValue?.toDouble()!! > totalAmount){
                        creditValues= creditValue?.toDouble()!! - totalAmount
                        prefHelper.put(Constant.PREF_CREDIT, creditValues.toString())
                    }else{
                        updatedCredit= totalAmount - creditValue?.toDouble()!!
                        prefHelper.put(Constant.PREF_CREDIT, "0")
                        creditValues=0.0
                    }

                    if(creditValues>0) {
                        callOrderUpdateAPI("Success")
                    }else if(creditValues==0.0) {
                        payment(updatedCredit)
                    }else{
                        payment(amount)
                    }

                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "" +"S", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "" + "Something.....", Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    @SuppressLint("SuspiciousIndentation", "NewApi")
    fun callOrderUpdateAPI(s: String) {
        dataBase = initializeDB(context)
        CoroutineScope(Dispatchers.IO).launch {

                orderMasterModel = OrderMasterModel(
                    last_order_master_id.toInt(),
                    last_id.toString() ,
                    user_id.toString(),
                    CommonClass.currentDate().toString(),
                    totalAmount.toString(),
                    "",
                    prefHelper.getString(Constant.PREF_ACTIVE).toString(),
                    "",
                    prefHelper.getString(Constant.PREF_LATITUDE).toString(),
                    prefHelper.getString(Constant.PREF_LONGITUDE).toString(),
                    s
                )
                PaymentGatewayRepository.updateOrderMaster(context, orderMasterModel)
                var update_order_id = PaymentGatewayRepository.updateOrderMasterAPI(context,orderMasterModel)
                if (update_order_id> 0) {
                    orderDetailsArrayList= PaymentGatewayRepository.getOrderDetailsList(context, update_order_id) as ArrayList<OrderDetailsModel>
                    for (i in orderDetailsArrayList) {
                        orderDetailsModel = OrderDetailsModel(
                            i.local_id,
                            i.id,
                            i.order_id,
                            i.product_id, i.price,
                            i.quantity,
                            s,
                            i.price* i.quantity,
                            user_id,
                            prefHelper.getString(Constant.PREF_ACTIVE).toString()
                        )
                        PaymentGatewayRepository.updateCartPaymentStatusbyId(i.product_id.toInt(),s, context)
                        PaymentGatewayRepository.updateCreditInUserMaster(user_id,creditValues.toString(), context)
                        PaymentGatewayRepository.updateOrderDetails(context, orderDetailsModel)
                        var updated_order_details_id= PaymentGatewayRepository.updateOrderDetailsAPI(context,orderDetailsModel)
                        if(updated_order_details_id>0){
                            Handler(Looper.getMainLooper()).post {
                                if(s.equals("Success")) {
                                    showDialog(
                                        "Transaction Complete",
                                        "Your payment has been successful."
                                    )
                                }else{
                                    showDialog("Transaction Failed", "Your payment has been failed")
                                }
                            }
                        } else{
                            Handler(Looper.getMainLooper()).post {
                                showDialog("Transaction Failed" + s, "Your payment has been failed")
                            }
                        }
                    }
                } else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Something went wrong...", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showDialog(s: String, message: String) {
        SweetAlertDialog(context,
            SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText(s)
            .setContentText(message)
            .setConfirmText("Ok")
            .setConfirmClickListener {sdialog ->
                context.startActivity(Intent(context, HomeScreen::class.java))
            }
            .show()
    }
}
