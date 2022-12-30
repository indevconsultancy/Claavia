package com.indev.claraa.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.indev.claraa.R
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
import com.indev.claraa.util.CommonClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RegistrationViewModel (val context: Context): ViewModel() {

    var shopName: ObservableField<String> = ObservableField("")
    var username: ObservableField<String> = ObservableField("")
    var password: ObservableField<String> = ObservableField("")
    var confirmPassword: ObservableField<String> = ObservableField("")
    var ownerName: ObservableField<String> = ObservableField("")
    var gender = ""
    var email: ObservableField<String> = ObservableField("")
    var mobNo: ObservableField<String> = ObservableField("")
    var etAddress: ObservableField<String> = ObservableField("")
    var pinCode: ObservableField<String> = ObservableField("")
    var spnState: String? = null
    var spnDistrict: String? = null
    lateinit var userRegistrationTable: UserRegistrationModel
    val readAllData: LiveData<UserRegistrationModel>
    lateinit var prefHelper: PrefHelper
    var checkLogin: Boolean = false

    var lat=""
    var long=""
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

    @SuppressLint("SuspiciousIndentation")
    fun btnSubmit(){
        prefHelper = PrefHelper(context)
        checkLogin = prefHelper.getBoolean(Constant.PREF_IS_LOGIN)
        lat = prefHelper.getString(Constant.PREF_LATITUDE).toString()
        long = prefHelper.getString(Constant.PREF_LONGITUDE).toString()
        gender= UserRegistration.gender
        if(checkValidation()) {
            SweetDialog.showProgressDialog(context)
            if (checkLogin == false) {
            SweetDialog.showProgressDialog(context)
            prefHelper = PrefHelper(context)
            userRegistrationTable = UserRegistrationModel(
            0,
            shopName.get().toString(),
            ownerName.get().toString(),
            "",
            password.get().toString(),
            email.get().toString(),
            mobNo.get().toString(),
            etAddress.get().toString(),
            UserRegistration.state_id.toString(),
            UserRegistration.district_id.toString(),prefHelper.getString(Constant.PREF_ACTIVE).toString(), CommonClass.currentDate().toString(), gender,lat,long,prefHelper.getString(Constant.PREF_CREDIT).toString(),
            "","",pinCode.get().toString(),"","",
                "","",""
        )
        viewModelScope.launch {
            UserRegistrationRepository.insertUserData(context, userRegistrationTable)
            var last_insert_id=0
            last_insert_id = UserRegistrationRepository.userRegistrationAPI(userRegistrationTable)
                if (last_insert_id> 0) {
                    context.startActivity(Intent(context, LoginScreen::class.java))
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "User successfully registered...", Toast.LENGTH_LONG).show()
                    }
                }else if (last_insert_id == 2) {
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

        else {
            var user_id= prefHelper.getString(Constant.PREF_USERID)
            userRegistrationTable = UserRegistrationModel(
                user_id!!.toInt(),
                shopName.get().toString(),
                ownerName.get().toString(),
                username.get().toString(),
                password.get().toString(),
                email.get().toString(),
                mobNo.get().toString(),
                etAddress.get().toString(),
                UserRegistration.state_id.toString(),
                UserRegistration.district_id.toString(),
                etAddress.get().toString(),CommonClass.currentDate().toString(),gender, prefHelper.getString(Constant.PREF_LATITUDE).toString(),prefHelper.getString(Constant.PREF_LONGITUDE).toString(),prefHelper.getString(Constant.PREF_CREDIT).toString(),
                "","",pinCode.get().toString(),"","",
                "","",""
            )
            userRegistrationTable.shop_name = shopName.get().toString()
            userRegistrationTable.user_name = username.get().toString()
            userRegistrationTable.owner_name = ownerName.get().toString()
            userRegistrationTable.email = email.get().toString()
            userRegistrationTable.mobile_number = mobNo.get().toString()
            userRegistrationTable.address = etAddress.get().toString()
            userRegistrationTable.state_id = UserRegistration.state_id.toString()
            userRegistrationTable.district_id = UserRegistration.district_id.toString()
            userRegistrationTable.latitude = lat
            userRegistrationTable.longitude = long
            userRegistrationTable.pinCode = pinCode.get().toString()

            viewModelScope.launch {

                CoroutineScope(Dispatchers.IO).launch {
                    UserRegistrationRepository.updateData(context, userRegistrationTable)
                    var last_user_id=0
                    last_user_id = UserRegistrationRepository.userProfileUpdateAPI(context,userRegistrationTable)
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
                    SweetDialog.dismissDialog()
                }
            }
          }
        }
    }




    fun getStateList(context: Context): List<StateModel>? {
        return UserRegistrationRepository.getStateList(context)
    }
    fun getDistrictList(context: Context, state_id: Int): List<DistrictModel>? {
        return UserRegistrationRepository.getDistrictList(context,state_id)
    }

    private fun checkValidation(): Boolean {
        if(shopName.get()?.isEmpty()==true) {
            Toast.makeText(context, "Please enter shop name..", Toast.LENGTH_SHORT).show()
            return false
        }

        if(ownerName.get()?.isEmpty()==true) {
            Toast.makeText(context, "Please enter owner name..", Toast.LENGTH_SHORT).show()
            return false
        }

        if(email.get()?.isEmpty()==true) {
            Toast.makeText(context, "Please enter email..", Toast.LENGTH_SHORT).show()
            return false
        }
        if (checkLogin == false) {
            if (password.get()?.isEmpty() == true) {
                Toast.makeText(context, "Please enter password..", Toast.LENGTH_SHORT).show()
                return false
            }else if (password.get().toString().length<8) {
                Toast.makeText(context, "Please enter password..", Toast.LENGTH_SHORT).show()
                return false
            }
        }

        if(mobNo.get()?.isEmpty()==true) {
            Toast.makeText(context, "Please enter mobile number..", Toast.LENGTH_SHORT).show()
            return false
        }else if(mobNo.get().toString().length<10) {
            Toast.makeText(context, "Please enter valid mobile number..", Toast.LENGTH_SHORT).show()
            return false
        }
        if(spnState.toString().trim()=="Select State"){
            Toast.makeText(context, "Please select state..", Toast.LENGTH_SHORT).show()
            return false
        }
        if(spnDistrict.toString().trim()=="Select District"){
            Toast.makeText(context, "Please select district..", Toast.LENGTH_SHORT).show()
            return false
        }
        if(etAddress.get()?.isEmpty()==true) {
            Toast.makeText(context, "Please enter address..", Toast.LENGTH_SHORT).show()
            return false
        }

        if(gender.length==0) {
            Toast.makeText(context, "Please choose gender..", Toast.LENGTH_SHORT).show()
            return false
        }

        if(pinCode.get()?.isEmpty()==true) {
            Toast.makeText(context, "Please enter pin code..", Toast.LENGTH_SHORT).show()
            return false
        }else if (pinCode.get()?.length!!<6){
            Toast.makeText(context,"Please enter valid pin code",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun replaceFregment(fragment : Fragment) {
        var transaction = (context as HomeScreen).supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }
}