package com.indev.claraa.viewmodel

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R
import com.indev.claraa.adapter.AddressDetailsAdapter
import com.indev.claraa.entities.AddressDetailsModel
import com.indev.claraa.entities.UserRegistrationModel
import com.indev.claraa.fragment.AddressList
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.AddressDetailsRepository
import com.indev.claraa.repository.UserRegistrationRepository
import com.indev.claraa.ui.HomeScreen
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
    var districtOfUser: String? = null
    var stateOfUser: String? = null
    lateinit var addressDetailsModel: AddressDetailsModel
    var local_id = 0
    lateinit var prefHelper: PrefHelper
    val readAllData: LiveData<AddressDetailsModel>

    init {
        local_id = AddressDetailsAdapter.local_id
        readAllData = AddressDetailsRepository.getAddressDatabyLocalID(context, local_id)!!
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
        var user_id = prefHelper.getInt(Constant.PREF_USERID)
        local_id = AddressDetailsAdapter.local_id
        if(local_id== 0) {
            insertAddress(user_id)
        }else{
            updateAddress(local_id, user_id!!)
        }
    }

    private fun updateAddress(local_id: Int, user_id: Int) {
        addressDetailsModel = AddressDetailsModel(
            local_id,0,user_id,
            shopName.get().toString(),
            personName.get().toString(),
            mobNo.get().toString(),
            etAddress1.get().toString(),stateOfUser.toString(),
            districtOfUser.toString(),pinCode.get().toString(),
            etAddress2.get().toString(),"","","",
            landmark.get().toString(),
        )
        //AddressDetailsRepository.editAddress(addressDetailsModel, context)

        viewModelScope.launch {
            AddressDetailsRepository.editAddress(addressDetailsModel, context)
            var last_id=0
            CoroutineScope(Dispatchers.IO).launch {
                last_id = AddressDetailsRepository.addressUpdateApi(addressDetailsModel)
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
            }
        }
    }

    private fun insertAddress(user_id: Int?) {
        addressDetailsModel = AddressDetailsModel(0,0,0,
            shopName.get().toString(),
            personName.get().toString(),
            mobNo.get().toString(),
            etAddress1.get().toString(),stateOfUser.toString(),
            districtOfUser.toString(),pinCode.get().toString(),
            etAddress2.get().toString(),"","","",
            landmark.get().toString(),
        )
        viewModelScope.launch {
            AddressDetailsRepository.insertAddressData(context, addressDetailsModel)
            var last_id=0
            CoroutineScope(Dispatchers.IO).launch {
                last_id = AddressDetailsRepository.userAddressDetailsAPI(addressDetailsModel)
                if (last_id> 0) {
                    replaceFregment(AddressList())
                    AddressDetailsRepository.getAddressDatabyLocalID(context,last_id)
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Successfully Address Registered", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Invalid user", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


    private fun replaceFregment(fragment : Fragment) {
        var transaction = (context as HomeScreen).supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }

    override fun onClickListner(id: Int) {
    }

    override fun updateTextView(amount: Int) {
    }

    override fun updatePowerRange(power_range: String) {
    }


}