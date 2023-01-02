package com.indev.claraa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.bumptech.glide.Glide
import com.indev.claraa.R
import com.indev.claraa.entities.OrderDetailsModel
import com.indev.claraa.entities.OrderMasterModel
import com.indev.claraa.fragment.ProductDetails
import com.indev.claraa.repository.OrderHistoryReposetory
import com.indev.claraa.restApi.ClientApi
import com.indev.claraa.ui.HomeScreen

class OrderListAdapter (private val context: Context, var orderListArrayList: ArrayList<OrderMasterModel>, private val listener: ClickLinstener) : RecyclerView.Adapter<OrderListAdapter.MyViewholder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_order_list, parent, false)
        return MyViewholder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val currentItem = orderListArrayList[position]
        holder.tv_id.text =": "+currentItem.order_id
        holder.tv_date.text =": "+currentItem.order_date
        holder.tv_amount.text = ": "+currentItem.order_amount
        holder.btnView.setOnClickListener(){
            listener.onClickListner(currentItem.order_id.toInt())
        }

    }


    fun setData(orderMasterModel: ArrayList<OrderMasterModel>) {
        this.orderListArrayList= orderMasterModel
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return orderListArrayList.size
    }

    inner class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_id: TextView = itemView!!.findViewById(R.id.tv_id)
        val tv_date: TextView = itemView!!.findViewById(R.id.tv_date)
        val tv_amount: TextView = itemView!!.findViewById(R.id.tv_amount)
        val btnView: Button = itemView!!.findViewById(R.id.btnView)
        init {
            itemView.setOnClickListener {
                listener.onClickListner(adapterPosition)
            }
        }
    }

}