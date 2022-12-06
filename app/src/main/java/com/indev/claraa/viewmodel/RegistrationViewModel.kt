package com.indev.claraa.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.indev.claraa.entities.UserRegistrationModel
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.UserRegistrationRepository
import com.indev.claraa.ui.LoginScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RegistrationViewModel (val context: Context): ViewModel() {

    var shopName: ObservableField<String> = ObservableField("")
    var personName: ObservableField<String> = ObservableField("")
    var email: ObservableField<String> = ObservableField("")
    var mobNo: ObservableField<String> = ObservableField("")
    var etAddress: ObservableField<String> = ObservableField("")
    var pinCode: ObservableField<String> = ObservableField("")
    var districtOfUser: String? = null
    var stateOfUser: String? = null
     lateinit var userRegistrationTable: UserRegistrationModel
    val readAllData: LiveData<UserRegistrationModel>
    lateinit var prefHelper: PrefHelper
    var checkLogin: Boolean = false

    init {
        readAllData = UserRegistrationRepository.getRegistrationData(context)!!
    }

    val clicksListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            districtOfUser = parent?.getItemAtPosition(position) as String
            Log.d("TAG", "onItemSelected: " + districtOfUser)
        }
    }

    val clickListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            stateOfUser = parent?.getItemAtPosition(position) as String
            Log.d("TAG", "onItemSelected: " + stateOfUser)
        }
    }



    fun btnSubmit() {
        prefHelper= PrefHelper(context)
        checkLogin = prefHelper.getBoolean(Constant.PREF_IS_LOGIN)

        if(checkLogin == false) {
            userRegistrationTable = UserRegistrationModel(
                0,
                0,
                shopName.get().toString(),
                personName.get().toString(),
                email.get().toString(),
                mobNo.get().toString(),
                etAddress.get().toString(),
                "",
                stateOfUser.toString(),
                districtOfUser.toString(),
                "",
                "",
                pinCode.get().toString()
            )
            viewModelScope.launch {
                UserRegistrationRepository.insertUserData(context, userRegistrationTable)
                //Data store in model
                var last_user_id=0
                CoroutineScope(Dispatchers.IO).launch {
                    last_user_id = UserRegistrationRepository.userRegistrationAPI(userRegistrationTable)
                    if (last_user_id> 0) {
                        context.startActivity(Intent(context, LoginScreen::class.java))

                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(context, "Successfully Registered", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(context, "Invalid user", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }else{
            userRegistrationTable = UserRegistrationModel(
                0,
                0,
                shopName.get().toString(),
                personName.get().toString(),
                email.get().toString(),
                mobNo.get().toString(),
                etAddress.get().toString(),
                "",
                stateOfUser.toString(),
                districtOfUser.toString(),
                "",
                "",
                pinCode.get().toString()
            )
            userRegistrationTable.shop_name = shopName.get().toString()
            userRegistrationTable.user_name = shopName.get().toString()
            userRegistrationTable.email = shopName.get().toString()
            userRegistrationTable.mobile_number = shopName.get().toString()
            userRegistrationTable.address = shopName.get().toString()
            userRegistrationTable.city = shopName.get().toString()
            userRegistrationTable.state = shopName.get().toString()
            userRegistrationTable.district = shopName.get().toString()
            userRegistrationTable.pinCode = shopName.get().toString()
            userRegistrationTable.register_date = shopName.get().toString()
            userRegistrationTable.active = shopName.get().toString()
            viewModelScope.launch {
                UserRegistrationRepository.updateData(context, userRegistrationTable)
                context.startActivity(Intent(context, LoginScreen::class.java))
            }
        }
    }

}