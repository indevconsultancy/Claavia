package com.indev.claraa.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R
import com.indev.claraa.SweetDialog
import com.indev.claraa.adapter.AddressDetailsAdapter
import com.indev.claraa.entities.AddressDetailsModel
import com.indev.claraa.entities.DistrictModel
import com.indev.claraa.entities.StateModel
import com.indev.claraa.fragment.AddNewAddress
import com.indev.claraa.fragment.AddressList
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.AddressDetailsRepository
import com.indev.claraa.ui.HomeScreen
import com.indev.claraa.util.CommonClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddressViewModel (val context: Context): ViewModel(), ClickLinstener {
    var shopName: ObservableField<String> = ObservableField("")
    var personName: ObservableField<String> = ObservableField("")
    var email: ObservableField<String> = ObservableField("")
    var mobNo: ObservableField<String> = ObservableField("")
    var etAddress1: ObservableField<String> = ObservableField("")
    var etAddress2: ObservableField<String> = ObservableField("")
    var pinCode: ObservableField<String> = ObservableField("")
    var landmark: ObservableField<String> = ObservableField("")
    var spnDistrict: String? = null
    var spnState: String? = null
    lateinit var addressDetailsModel: AddressDetailsModel
    var local_id = 0
    var id = "0"
    lateinit var prefHelper: PrefHelper
    val readAllData: LiveData<AddressDetailsModel>

    init {
        local_id = AddressDetailsAdapter.local_id
        readAllData = AddressDetailsRepository.getAddressDatabyLocalID(context, local_id)!!
    }

//    val clickListener = object : AdapterView.OnItemSelectedListener {
//        override fun onNothingSelected(parent: AdapterView<*>?) {
//
//        }
//
//        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            stateOfUser = parent?.getItemAtPosition(position) as String
//            Log.d("TAG", "onItemSelected: " + stateOfUser)
//        }
//    }


    fun btnSubmit() {
        prefHelper = PrefHelper(context)
        var user_id = prefHelper.getString(Constant.PREF_USERID)
        local_id = AddressDetailsAdapter.local_id
        id = AddressDetailsAdapter.id
        if(local_id== 0) {
            insertAddress(user_id!!)
        }else{
            updateAddress(local_id,id, user_id!!)
        }
    }

    private fun updateAddress(local_id: Int, id: String, userId: String) {
        SweetDialog.showProgressDialog(context)
        addressDetailsModel = AddressDetailsModel(
            local_id,id,userId.toInt(),
            shopName.get().toString(),
            personName.get().toString(),
            mobNo.get().toString(),
            etAddress1.get().toString(),AddNewAddress.state_id.toString(),
            AddNewAddress.district_id.toString(),pinCode.get().toString(),
            etAddress2.get().toString(),prefHelper.getString(Constant.PREF_LATITUDE).toString(),prefHelper.getString(Constant.PREF_LONGITUDE).toString(),CommonClass.currentDate().toString(),
            landmark.get().toString(),
        )
        //AddressDetailsRepository.editAddress(addressDetailsModel, context)

        viewModelScope.launch {
            AddressDetailsRepository.editAddress(addressDetailsModel, context)
            var last_id=0
            CoroutineScope(Dispatchers.IO).launch {
                last_id = AddressDetailsRepository.addressUpdateApi(context,addressDetailsModel)
                if (last_id> 0) {
                    replaceFregment(AddressList())

                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Successfully Address Updated", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Invalid user", Toast.LENGTH_LONG).show()
                    }
                }
                SweetDialog.dismissDialog()
            }
        }
    }



    @SuppressLint("NewApi")
    private fun insertAddress(user_id: String) {
        if (checkValidation()){
            SweetDialog.showProgressDialog(context)
        var id= CommonClass.getUniqueId()
        addressDetailsModel = AddressDetailsModel(0, id.toString(),user_id.toInt(),
            shopName.get().toString(),
            personName.get().toString(),
            mobNo.get().toString(),
            etAddress1.get().toString(),AddNewAddress.state_id.toString(),
            AddNewAddress.district_id.toString(),pinCode.get().toString(),
            etAddress2.get().toString(),prefHelper.getString(Constant.PREF_LATITUDE).toString(),prefHelper.getString(Constant.PREF_LONGITUDE).toString(),CommonClass.currentDate().toString(),
            landmark.get().toString()
        )
        viewModelScope.launch {
            AddressDetailsRepository.insertAddressData(context, addressDetailsModel)
            var last_id=0
            CoroutineScope(Dispatchers.IO).launch {
                last_id = AddressDetailsRepository.userAddressDetailsAPI(context,addressDetailsModel)
                if (last_id> 0) {
                    replaceFregment(AddressList())
                    AddressDetailsRepository.updateAddressId(last_id,id.toString(), context)
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Successfully Address Registered", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Invalid user", Toast.LENGTH_LONG).show()
                    }

                }
                SweetDialog.dismissDialog()
            }

        }
        }
    }


    fun getStateList(context: Context): List<StateModel>? {
        return AddressDetailsRepository.getStateList(context)
    }
    fun getDistrictList(context: Context,state_id:Int): List<DistrictModel>? {
        return AddressDetailsRepository.getDistrictList(context,state_id)
    }


    private fun replaceFregment(fragment : Fragment) {
        var transaction = (context as HomeScreen).supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }

    private fun checkValidation(): Boolean{
       if (shopName.get()?.isEmpty() ==true) {
           Toast.makeText(context,"Please enter business name",Toast.LENGTH_SHORT).show()
          return false
       }
        if (personName.get()?.isEmpty()==true){
            Toast.makeText(context,"Please enter owner name",Toast.LENGTH_SHORT).show()
            return false
        }
        if (mobNo.get()?.isEmpty()==true){
            Toast.makeText(context,"Please enter mobile number",Toast.LENGTH_SHORT).show()
            return false
        }else if (mobNo.get()?.length!!<10){
            Toast.makeText(context,"Please enter valid mobile number",Toast.LENGTH_SHORT).show()
            return false
        }

        if (AddNewAddress.state_id.toString().isEmpty()){
            Toast.makeText(context,"Please select state",Toast.LENGTH_SHORT).show()
            return false
        }
        if (AddNewAddress.district_id.toString().isEmpty()){
            Toast.makeText(context,"Please select district",Toast.LENGTH_SHORT).show()
            return false
        }

        if (etAddress1.get()?.isEmpty()==true){
            Toast.makeText(context,"Please enter address name",Toast.LENGTH_SHORT).show()
            return false
        }
        if (etAddress2.get()?.isEmpty()==true){
            Toast.makeText(context,"Please enter street",Toast.LENGTH_SHORT).show()
            return false
        }
        if (landmark.get()?.isEmpty()==true){
            Toast.makeText(context,"Please enter landmark",Toast.LENGTH_SHORT).show()
            return false
        }
        if (pinCode.get()?.isEmpty()==true){
            Toast.makeText(context,"Please enter pin code",Toast.LENGTH_SHORT).show()
            return false
        }else if (pinCode.get()?.length!!<6){
            Toast.makeText(context,"Please enter valid pin code",Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    override fun onClickListner(id: Int) {
    }

    override fun updateTextInteger(id: Int) {
    }

    override fun updateTextString(power_range: String) {
    }

    override fun callUpdateCart(id: Int, qty: String) {
    }


}