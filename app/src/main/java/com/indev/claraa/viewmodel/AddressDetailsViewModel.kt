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
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indev.claraa.R
import com.indev.claraa.entities.AddressDetailsModel
import com.indev.claraa.fragment.AddressList
import com.indev.claraa.repository.AddressDetailsRepository
import com.indev.claraa.repository.UserRegistrationRepository
import com.indev.claraa.ui.HomeScreen
import com.indev.claraa.ui.LoginScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddressDetailsViewModel (val context: Context): ViewModel() {
    var shopName: ObservableField<String> = ObservableField("")
    var personName: ObservableField<String> = ObservableField("")
    var email: ObservableField<String> = ObservableField("")
    var mobNo: ObservableField<String> = ObservableField("")
    var etAddress: ObservableField<String> = ObservableField("")
    var pinCode: ObservableField<String> = ObservableField("")
    var areaVillage: ObservableField<String> = ObservableField("")
    var landmark: ObservableField<String> = ObservableField("")
    var districtOfUser: String? = null
    var stateOfUser: String? = null
    lateinit var addressDetailsModel: AddressDetailsModel
//    val readData: LiveData<AddressDetailsModel>

//    init {
//        readData = AddressDetailsRepository.getAddressData(context)!!
//    }

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
        addressDetailsModel = AddressDetailsModel(0,0,
            shopName.get().toString(),
            personName.get().toString(),
            email.get().toString(),
            mobNo.get().toString(),
            etAddress.get().toString(),
            "",stateOfUser.toString(),
            districtOfUser.toString(),pinCode.get().toString(),
            areaVillage.get().toString(),
            landmark.get().toString(),
            )
        viewModelScope.launch {
            AddressDetailsRepository.insertAddressData(context, addressDetailsModel)
            var last_insert_id=0
            CoroutineScope(Dispatchers.IO).launch {
                last_insert_id = AddressDetailsRepository.userAddressDetailsAPI(addressDetailsModel)
                if (last_insert_id> 0) {
                    replaceFregment(AddressList())

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
    }




    private fun replaceFregment(fragment : Fragment) {
        var transaction = (context as HomeScreen).supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }


}