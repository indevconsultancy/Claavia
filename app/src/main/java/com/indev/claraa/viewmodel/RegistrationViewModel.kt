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
import com.indev.claraa.SweetDialog
import com.indev.claraa.entities.DistrictModel
import com.indev.claraa.entities.StateModel
import com.indev.claraa.entities.UserRegistrationModel
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.UserRegistrationRepository
import com.indev.claraa.ui.HomeScreen
import com.indev.claraa.ui.LoginScreen
import com.indev.claraa.ui.UserRegistration
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
    lateinit var userRegistrationTable: UserRegistrationModel
    val readAllData: LiveData<UserRegistrationModel>
    lateinit var prefHelper: PrefHelper
    var checkLogin: Boolean = false

    init {
        readAllData = UserRegistrationRepository.getRegistrationData(context)!!
    }



//    val clicksListener = object : AdapterView.OnItemSelectedListener {
//        override fun onNothingSelected(parent: AdapterView<*>?) {
//
//        }
//
//        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            districtOfUser = parent?.getItemAtPosition(position) as String
//            Log.d("TAG", "onItemSelected: " + districtOfUser)
//        }
//    }



    fun btnJoin() {
        prefHelper = PrefHelper(context)
        checkLogin = prefHelper.getBoolean(Constant.PREF_IS_LOGIN)

        if (checkLogin == true) {
            userRegistrationTable = UserRegistrationModel(
                prefHelper.getInt(Constant.PREF_USERID)!!,
                shopName.get().toString(),
                ownerName.get().toString(),
                username.get().toString(),
                "amit123",
                email.get().toString(),
                mobNo.get().toString(),
                etAddress.get().toString(),
                UserRegistration.state_id.toString(),
                UserRegistration.district_id.toString(),
                etAddress.get().toString(), "17-12-2022", "male", "", "",
                pinCode.get().toString()
            )
            userRegistrationTable.shop_name = shopName.get().toString()
            userRegistrationTable.user_name = username.get().toString()
            userRegistrationTable.owner_name = ownerName.get().toString()
            userRegistrationTable.email = email.get().toString()
            userRegistrationTable.mobile_number = mobNo.get().toString()
            userRegistrationTable.address = etAddress.get().toString()
            userRegistrationTable.state_id = UserRegistration.state_id.toString()
            userRegistrationTable.district_id = UserRegistration.district_id.toString()
            userRegistrationTable.pinCode = pinCode.get().toString()

            viewModelScope.launch {

                CoroutineScope(Dispatchers.IO).launch {
                    UserRegistrationRepository.updateData(context, userRegistrationTable)
                    var last_user_id=0
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

    fun btnSubmit(){
        SweetDialog.showProgressDialog(context)
        //       if(checkValidation()) {
        prefHelper = PrefHelper(context)
        userRegistrationTable = UserRegistrationModel(
            prefHelper.getInt(Constant.PREF_USERID)!!,
            shopName.get().toString(),
            ownerName.get().toString(),
            username.get().toString(),
            "amit123",
            email.get().toString(),
            mobNo.get().toString(),
            etAddress.get().toString(),
            UserRegistration.state_id.toString(),
            UserRegistration.district_id.toString(),
            etAddress.get().toString(), "17-12-2022", "male", "", "",
            pinCode.get().toString()
        )
        viewModelScope.launch {
            UserRegistrationRepository.insertUserData(context, userRegistrationTable)
            var last_user_id=0
                last_user_id = UserRegistrationRepository.userRegistrationAPI(userRegistrationTable)
                if (last_user_id> 0) {
                    context.startActivity(Intent(context, LoginScreen::class.java))
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "User successfully registered...", Toast.LENGTH_LONG).show()
                    }
                }else if (last_user_id == 2) {
                    context.startActivity(Intent(context, LoginScreen::class.java))
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "User already registered...", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Something went wrong...", Toast.LENGTH_LONG).show()
                    }
                }
            SweetDialog.dismissDialog()
            }
    }

    fun getStateList(context: Context): List<StateModel>? {
        return UserRegistrationRepository.getStateList(context)
    }
    fun getDistrictList(context: Context): List<DistrictModel>? {
        return UserRegistrationRepository.getDistrictList(context)
    }

//    private fun checkValidation(): Boolean {
//        if(shopName.get().toString().length==0) {
//            Toast.makeText(context, "Please enter username..", Toast.LENGTH_SHORT).show()
//            return false
//        }
//
//        if(ownerName.get().toString().length<4) {
//            Toast.makeText(context, "Please enter email..", Toast.LENGTH_SHORT).show()
//            return false
//        }
//
//
//        if(username.get().toString().length<4) {
//            Toast.makeText(context, "Please enter password..", Toast.LENGTH_SHORT).show()
//            return false
//        }
//        if(email.get().toString().length<8) {
//            Toast.makeText(context, "Please enter password..", Toast.LENGTH_SHORT).show()
//            return false
//        }
//        return true
//    }

}