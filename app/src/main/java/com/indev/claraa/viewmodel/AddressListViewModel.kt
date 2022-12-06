package com.indev.claraa.viewmodel

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.indev.claraa.R
import com.indev.claraa.entities.AddressDetailsModel
import com.indev.claraa.fragment.AddNewAddress
import com.indev.claraa.repository.AddressDetailsRepository
import com.indev.claraa.ui.HomeScreen

class AddressListViewModel (val context: Context): ViewModel() {

   fun btnList(){
       replaceFregment(AddNewAddress())
   }

    private fun replaceFregment(fragment : Fragment) {
        var transaction = (context as HomeScreen).supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }

    fun getAddressData(context: Context): LiveData<List<AddressDetailsModel>>? {
        return AddressDetailsRepository.getAddressData(context)
    }
}