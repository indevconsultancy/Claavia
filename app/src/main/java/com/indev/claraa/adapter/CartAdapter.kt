package com.indev.claraa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R
import com.indev.claraa.entities.Cart

class CartAdapter(private val context: Context, var cartList: List<Cart>, private val listener: ClickLinstener) : RecyclerView.Adapter<CartAdapter.MyViewholder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_cart, parent, false)
        return MyViewholder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val currentItem = cartList[position]
        holder.tvRange.text ="Size: " + currentItem.ranges
    }

    fun setData(cart: ArrayList<Cart>) {
        this.cartList= cart
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return cartList.size
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

