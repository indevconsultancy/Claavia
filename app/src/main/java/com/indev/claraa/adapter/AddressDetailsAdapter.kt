package com.indev.claraa.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R
import com.indev.claraa.entities.AddressDetailsModel
import com.indev.claraa.fragment.AddNewAddress
import com.indev.claraa.fragment.OrderPlace
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.AddressDetailsRepository
import com.indev.claraa.repository.ProductRepository
import com.indev.claraa.ui.HomeScreen


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
        holder.mobNo.text = "Phone Number: " + currentItem.mobile_number
        holder.stateDistrict.text = currentItem.state_id + ", " + currentItem.district_id
        holder.address1.text = currentItem.address1 +"," + currentItem.address2 + "," +currentItem.landmark

        preferences= PrefHelper(context)
        holder.btnDelivery.setOnClickListener{
            preferences.put(Constant.PREF_IS_CHECK_CART, true)
            replaceFregment(OrderPlace())
        }

        holder.deleteAddress.setOnClickListener{
            listener.onClickListner(currentItem.id.toInt())
            //AddressDetailsRepository.deleteAddress(currentItem.local_id,context)
        }

        holder.editAddress.setOnClickListener{
            local_id =currentItem.local_id
            id =currentItem.id
            listener.onClickListner(currentItem.local_id)
            replaceFregment(AddNewAddress())
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
