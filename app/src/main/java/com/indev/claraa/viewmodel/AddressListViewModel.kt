package com.indev.claraa.viewmodel

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.indev.claraa.R
import com.indev.claraa.fragment.AddressDetails
import com.indev.claraa.ui.HomeScreen

class AddressListViewModel (val context: Context): ViewModel() {

   fun btnList(){
       replaceFregment(AddressDetails())
   }

    private fun replaceFregment(fragment : Fragment) {
        var transaction = (context as HomeScreen).supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }
}