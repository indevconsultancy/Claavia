package com.indev.claraa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.bumptech.glide.Glide
import com.indev.claraa.R
import com.indev.claraa.entities.OrderMasterModel
import com.indev.claraa.entities.ProductMasterModel
import com.indev.claraa.fragment.ProductDetails
import com.indev.claraa.restApi.ClientApi
import com.indev.claraa.ui.HomeScreen

class OrderHistoryAdapter (private val context: Context, var orderHistoryMasterModelArrayList: ArrayList<OrderMasterModel>, private val listener: ClickLinstener) : RecyclerView.Adapter<OrderHistoryAdapter.MyViewholder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_order_history, parent, false)
        return MyViewholder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val currentItem = orderHistoryMasterModelArrayList[position]
    }

    private fun replaceFregment(fragment : Fragment) {
        var transaction = (context as HomeScreen).supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }

    fun setData(orderMasterModel: ArrayList<OrderMasterModel>) {
        this.orderHistoryMasterModelArrayList= orderMasterModel
    }

    override fun getItemCount(): Int {
        return orderHistoryMasterModelArrayList.size
    }

    inner class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                listener.onClickListner(adapterPosition)
            }
        }
    }

    companion object{
    }

}