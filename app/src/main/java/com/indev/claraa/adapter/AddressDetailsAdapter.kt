package com.indev.claraa.adapter

import android.content.Context
import android.content.SharedPreferences
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
import com.indev.claraa.ui.HomeScreen


class AddressDetailsAdapter(private val context: Context, var addressDetailsModelList: ArrayList<AddressDetailsModel>, private val listener: ClickLinstener) : RecyclerView.Adapter<AddressDetailsAdapter.MyViewholder>(){

    lateinit var preferences: PrefHelper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_address_list, parent, false)
        return MyViewholder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val currentItem = addressDetailsModelList[position]
        holder.shopName.text = currentItem.shop_name
        holder.personName.text = currentItem.user_name
        holder.email.text = currentItem.email
        holder.mobNo.text = currentItem.mobile_number
        holder.pinCode.text = currentItem.pinCode
        holder.state.text = currentItem.state_id
        holder.district.text = currentItem.district_id
        holder.houseNo.text = currentItem.address1
        holder.area.text = currentItem.address2
        holder.landMark.text = currentItem.landmark
        holder.shopName.text = currentItem.shop_name

        preferences= PrefHelper(context)
        holder.btnDelivery.setOnClickListener{
            preferences.put(Constant.PREF_IS_CHECK_CART, true)
            replaceFregment(OrderPlace())
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
        val email: TextView = itemView!!.findViewById(R.id.email)
        val mobNo: TextView = itemView!!.findViewById(R.id.mobNo)
        val pinCode: TextView = itemView!!.findViewById(R.id.pinCode)
        val state: TextView = itemView!!.findViewById(R.id.state)
        val district: TextView = itemView!!.findViewById(R.id.district)
        val houseNo: TextView = itemView!!.findViewById(R.id.houseNo)
        val area: TextView = itemView!!.findViewById(R.id.area)
        val landMark: TextView = itemView!!.findViewById(R.id.landMark)
        val editAddress: Button = itemView!!.findViewById(R.id.editAddress)
        val btnDelivery: Button = itemView!!.findViewById(R.id.btnDelivery)

        init {
            itemView.setOnClickListener {
                listener.onClickListner(adapterPosition)
            }
        }
    }


}
