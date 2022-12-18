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
import com.indev.claraa.ui.HomeScreen
import com.indev.claraa.ui.LoginScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RegistrationViewModel (val context: Context): ViewModel() {

    var shopName: ObservableField<String> = ObservableField("")
    var username: ObservableField<String> = ObservableField("")
    var password: ObservableField<String> = ObservableField("")
    var confirmPassword: ObservableField<String> = ObservableField("")
    var ownerName: ObservableField<String> = ObservableField("")
    var gender: ObservableField<String> = ObservableField("")
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
        prefHelper = PrefHelper(context)
        checkLogin = prefHelper.getBoolean(Constant.PREF_IS_LOGIN)

        if (checkLogin == true) {
            userRegistrationTable = UserRegistrationModel(
                44,
                shopName.get().toString(),
                ownerName.get().toString(),
                username.get().toString(),
                "amit123",
                email.get().toString(),
                mobNo.get().toString(),
                etAddress.get().toString(),
                stateOfUser.toString(),
                districtOfUser.toString(),
                etAddress.get().toString(), "17-12-2022", "male", "", "",
                pinCode.get().toString()
            )
            userRegistrationTable.shop_name = shopName.get().toString()
            userRegistrationTable.user_name = username.get().toString()
            userRegistrationTable.owner_name = ownerName.get().toString()
            userRegistrationTable.email = email.get().toString()
            userRegistrationTable.mobile_number = mobNo.get().toString()
            userRegistrationTable.address = etAddress.get().toString()
            userRegistrationTable.state_id = stateOfUser.toString()
            userRegistrationTable.district_id = districtOfUser.toString()
            userRegistrationTable.pinCode = pinCode.get().toString()
            viewModelScope.launch {
                UserRegistrationRepository.updateData(context, userRegistrationTable)
                var last_user_id=0
                CoroutineScope(Dispatchers.IO).launch {
                    last_user_id = UserRegistrationRepository.userProfileUpdateAPI(userRegistrationTable)
                    if (last_user_id> 0) {
                        context.startActivity(Intent(context, HomeScreen::class.java))
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(context, "Successfully Updated..", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(context, "Something went wrong..", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    fun btnJoin(){
        if(checkValidation()) {
            userRegistrationTable = UserRegistrationModel(
                0,
                "","",
                username.get().toString(),
                password.get().toString(),
                email.get().toString(),
                "",
                etAddress.get().toString(),
                "",
                "",
                "", "17-12-2022", "male", "", "",
                ""
            )
            viewModelScope.launch {
                UserRegistrationRepository.insertUserData(context, userRegistrationTable)
                var last_user_id=0
                CoroutineScope(Dispatchers.IO).launch {
                    last_user_id = UserRegistrationRepository.userRegistrationAPI(userRegistrationTable)
                    if (last_user_id> 0) {
                        context.startActivity(Intent(context, LoginScreen::class.java))
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(context, "Successfully Registered..", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(context, "Something went wrong..", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun checkValidation(): Boolean {
        if(username.get()?.isEmpty() == true) {
            Toast.makeText(context, "Please enter username..", Toast.LENGTH_SHORT).show()
            return false
        }

        if(email.get()?.isEmpty() == true) {
            Toast.makeText(context, "Please enter email..", Toast.LENGTH_SHORT).show()
            return false
        }
        if(password.get()?.isEmpty() == true) {
            Toast.makeText(context, "Please enter password..", Toast.LENGTH_SHORT).show()
            return false
        }

        if(confirmPassword.get()?.isEmpty() == true) {
            Toast.makeText(context, "Please enter confirm password..", Toast.LENGTH_SHORT).show()
            return false
        }

        if(!password.get().equals(confirmPassword.get())) {
            Toast.makeText(context, "Password and confirm password do not match..", Toast.LENGTH_SHORT).show()
            return false
        }
        return true

    }

}