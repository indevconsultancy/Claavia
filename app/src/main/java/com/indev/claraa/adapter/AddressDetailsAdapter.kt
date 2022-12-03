package com.indev.claraa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R


class AddressDetailsAdapter (private val context: Context, var addressDetailsModelList: ArrayList<String>, private val listener: ClickLinstener) : RecyclerView.Adapter<AddressDetailsAdapter.MyViewholder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_address_list, parent, false)
        return MyViewholder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val currentItem = addressDetailsModelList[position]
        holder.newAddress.text = currentItem
        holder.newAddress.setOnClickListener{
//            replaceFregment(ProductDetails())
        }
    }

//    private fun replaceFregment(fragment : Fragment) {
//        var transaction = (context as AddressList).supportFragmentManager.beginTransaction()
//        transaction?.replace(R.id.frame_layout, fragment)
//        transaction.commit()
//    }

    fun setData(homeModel: ArrayList<String>) {
        this.addressDetailsModelList= homeModel
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return addressDetailsModelList.size
    }

    inner class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newAddress: TextView = itemView!!.findViewById(R.id.newAddress)
//        val tvProductName: TextView = itemView!!.findViewById(R.id.tvProductName)

        init {
            itemView.setOnClickListener {
                listener.onClickListner(adapterPosition)
            }
        }
    }


}
