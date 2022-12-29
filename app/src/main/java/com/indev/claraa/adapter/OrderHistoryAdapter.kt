package com.indev.claraa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.bumptech.glide.Glide
import com.indev.claraa.R
import com.indev.claraa.entities.OrderDetailsModel
import com.indev.claraa.repository.OrderHistoryReposetory
import com.indev.claraa.restApi.ClientApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OrderHistoryAdapter(private val context: Context, var orderDetailsArrayList: ArrayList<OrderDetailsModel>, private val listener: ClickLinstener) : RecyclerView.Adapter<OrderHistoryAdapter.MyViewholder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_order_history, parent, false)
        return MyViewholder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val currentItem = orderDetailsArrayList[position]
        var image=""
        var product_name= ""
        var range= ""
        var packSize= ""

        Thread(Runnable {
            Thread.sleep(1000)
            image= OrderHistoryReposetory.getImage(context, currentItem.product_id).toString()
            product_name= OrderHistoryReposetory.getProductName(context, currentItem.product_id).toString()
            holder.tvProductName.text = product_name
            var packID= OrderHistoryReposetory.getPacksID(context, currentItem.product_id)
            packSize= OrderHistoryReposetory.getPacksSize(context, packID.toString()).toString()
            holder.tvPackSize.text ="Packs Size: " + packSize
            range= OrderHistoryReposetory.getPowerSize(context, currentItem.product_id).toString()
            holder.tvPower.text ="Power: " + range
            Glide.with(context).load(ClientApi.BASE_IMAGE_URL + image).into(holder.imageProduct)

        })

        holder.tvTotalAmount.text ="Total Amount: " + currentItem.amount.toString()
        holder.tvQuantity.text ="Quantity: " + currentItem.quantity.toString()
    }

    fun setData(orderDetailsModel: ArrayList<OrderDetailsModel>) {
        this.orderDetailsArrayList= orderDetailsModel
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return orderDetailsArrayList.size
    }

    inner class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProductName: TextView = itemView!!.findViewById(R.id.tvProductName)
        val tvPower: TextView = itemView!!.findViewById(R.id.tvPower)
        val tvPackSize: TextView = itemView!!.findViewById(R.id.tvPackSize)
        val tvQuantity: TextView = itemView!!.findViewById(R.id.tvQuantity)
        val tvTotalAmount: TextView = itemView!!.findViewById(R.id.tvTotalAmount)
        val imageProduct: ImageView = itemView!!.findViewById(R.id.imageProduct)
        init {
            itemView.setOnClickListener {
                listener.onClickListner(adapterPosition)
            }
        }
    }

}