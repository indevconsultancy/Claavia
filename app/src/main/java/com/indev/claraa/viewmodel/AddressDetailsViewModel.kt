package com.indev.claraa.viewmodel

import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indev.claraa.R
import com.indev.claraa.entities.AddressDetailsModel
import com.indev.claraa.entities.CartModel
import com.indev.claraa.fragment.AddressDetails
import com.indev.claraa.fragment.AddressList
import com.indev.claraa.repository.AddressDetailsRepository
import com.indev.claraa.repository.CartRepository
import com.indev.claraa.repository.UserRegistrationRepository
import com.indev.claraa.ui.HomeScreen
import com.indev.claraa.ui.LoginScreen
import kotlinx.coroutines.launch

class AddressDetailsViewModel (val context: Context): ViewModel() {
    var shopName: ObservableField<String> = ObservableField("")
    var personName: ObservableField<String> = ObservableField("")
    var email: ObservableField<String> = ObservableField("")
    var mobNo: ObservableField<String> = ObservableField("")
    var etAddress: ObservableField<String> = ObservableField("")
    var pinCode: ObservableField<String> = ObservableField("")
    lateinit var addressDetailsModel: AddressDetailsModel


    fun getCartList(context: Context): LiveData<List<CartModel>>? {
        return CartRepository.getCartList(context)
    }

    fun btnSubmit() {
        addressDetailsModel = AddressDetailsModel(0,0,
            shopName.get().toString(),
            personName.get().toString(),
            email.get().toString(),
            mobNo.get().toString(),
            etAddress.get().toString(),
            "","","",pinCode.get().toString(),
            "",
            "","",
            )
        viewModelScope.launch {
            AddressDetailsRepository.insertAddressData(context, addressDetailsModel)
//            context.startActivity(Intent(context, AddressList::class.java))
            replaceFregment(AddressList())
        }
    }



    private fun replaceFregment(fragment : Fragment) {
        var transaction = (context as HomeScreen).supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }


}