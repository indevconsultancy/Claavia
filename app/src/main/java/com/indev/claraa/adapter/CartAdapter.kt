package com.indev.claraa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R
import com.indev.claraa.entities.CartModel

class CartAdapter(private val context: Context, var cartModelList: List<CartModel>, private val listener: ClickLinstener) : RecyclerView.Adapter<CartAdapter.MyViewholder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_cart, parent, false)
        return MyViewholder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val currentItem = cartModelList[position]
        holder.tvRange.text ="Size: " + currentItem.ranges
    }

    fun setData(cartModel: ArrayList<CartModel>) {
        this.cartModelList= cartModel
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return cartModelList.size
    }

   inner class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRange: TextView = itemView!!.findViewById(R.id.tvRange)

        init {
            itemView.setOnClickListener {
                listener.onClickListner(adapterPosition)
            }
        }
    }


}

