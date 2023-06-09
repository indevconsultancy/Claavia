package com.indev.claraa.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R
import com.indev.claraa.entities.AddressDetailsModel
import com.indev.claraa.entities.deleteModel
import com.indev.claraa.fragment.AddNewAddress
import com.indev.claraa.fragment.AddressList
import com.indev.claraa.fragment.OrderPlace
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.AddressDetailsRepository
import com.indev.claraa.ui.HomeScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddressDetailsAdapter(private val context: Context, var addressDetailsModelList: ArrayList<AddressDetailsModel>, private val listener: ClickLinstener) : RecyclerView.Adapter<AddressDetailsAdapter.MyViewholder>(){

    lateinit var preferences: PrefHelper
    var localId=0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_address_list, parent, false)
        return MyViewholder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        local_id =0
        val currentItem = addressDetailsModelList[position]
        holder.shopName.text = currentItem.shop_name
        holder.personName.text = currentItem.user_name
        holder.mobNo.text = "Mobile Number: " + currentItem.mobile_number
        GlobalScope.launch {
            var state_name= AddressDetailsRepository.getStateName(context, currentItem.state_id)
            var district_name= AddressDetailsRepository.getDistrictName(context, currentItem.district_id)
            holder.stateDistrict.text = state_name + ", " + district_name
        }
        holder.address1.text = currentItem.address1 +"," + currentItem.address2 + "," +currentItem.landmark
        holder.pinCode.text = currentItem.pinCode

        preferences= PrefHelper(context)
        holder.btnDelivery.setOnClickListener{
            preferences.put(Constant.PREF_IS_CHECK_CART, true)
            preferences.put(Constant.PREF_ADDRESS_ID, currentItem.id)
            replaceFregment(OrderPlace())
        }



        holder.deleteAddress.setOnClickListener{
            deletePopupShow(currentItem.id)
        //            AddressDetailsRepository.deleteAddress(currentItem.local_id,context)
        }

        holder.editAddress.setOnClickListener{
            preferences.put(Constant.PREF_NEWADDRESS, false)
            local_id =currentItem.local_id
            id =currentItem.id
            replaceFregment(AddNewAddress())
        }
    }

    private fun deletePopupShow(id: String) {
        SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE).setTitleText("")
            .setContentText("Are you sure you want to delete the address?").setCancelText("Cancel")
            .setConfirmText("Ok")
            .setConfirmClickListener { sDialog ->
                listener.updateTextInteger(0)
                deleteAddress(id)
                sDialog.dismiss()
            }
            .showCancelButton(true)
            .setCancelClickListener { sDialog -> // Showing simple toast message to user
                sDialog.cancel()
            }.show()
    }

    private fun deleteAddress(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            AddressDetailsRepository.deleteAddress(id,context)
            var deleteModel= deleteModel(id)
            var last_id=0
            last_id = AddressDetailsRepository.addressDeleteApi(context,deleteModel)
            if (last_id> 0) {
                replaceFregment(AddressList())
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "Successfully Address Deleted", Toast.LENGTH_LONG).show()
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "Invalid user", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun replaceFregment(fragment : Fragment) {
        var transaction = (context as HomeScreen).supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }

    fun setData(homeModel: ArrayList<AddressDetailsModel>) {
        this.addressDetailsModelList= homeModel
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return addressDetailsModelList.size
    }

    inner class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val shopName: TextView = itemView!!.findViewById(R.id.shopName)
        val personName: TextView = itemView!!.findViewById(R.id.PersonName)
        val mobNo: TextView = itemView!!.findViewById(R.id.mobNo)
        val stateDistrict: TextView = itemView!!.findViewById(R.id.tvStateDistrict)
        val address1: TextView = itemView!!.findViewById(R.id.tvAddress1)
        val pinCode: TextView = itemView!!.findViewById(R.id.pinCode)
        val editAddress: Button = itemView!!.findViewById(R.id.editAddress)
        val btnDelivery: Button = itemView!!.findViewById(R.id.btnDelivery)
        val deleteAddress: Button = itemView!!.findViewById(R.id.deleteAddress)

        init {
            itemView.setOnClickListener {
                listener.onClickListner(adapterPosition)
            }
        }
    }

    companion object{
        var local_id= 0
        var id= "0"
    }

}
