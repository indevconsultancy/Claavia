package com.indev.claraa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.bumptech.glide.load.engine.Resource
import com.indev.claraa.R
import com.indev.claraa.entities.CartModel

class CartAdapter(private val context: Context, var cartModelList: List<CartModel>, private val listener: ClickLinstener) : RecyclerView.Adapter<CartAdapter.MyViewholder>(){
    var count =1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_cart, parent, false)
        return MyViewholder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val currentItem = cartModelList[position]
        holder.tvRange.text ="Size: " + currentItem.ranges

        holder.addButton.setOnClickListener {
            if(count > 0){
                increamentCount()
                holder.tvCount.setText("" + count)
                holder.deleteButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_remove_24));
            }else{
                holder.deleteButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_outline_24));
                holder.tvCount.setText("" + 1)
            }
        }

        holder.deleteButton.setOnClickListener {
            if(count >0){
                decreamentCount()
                holder.tvCount.setText("" + count)
                holder.deleteButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_remove_24));
            }else{
                holder.deleteButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_outline_24));
                holder.tvCount.setText("" + 1)
            }
        }

    }

    fun increamentCount() = count++
    fun decreamentCount() = count--

    fun setData(cartModel: ArrayList<CartModel>) {
        this.cartModelList= cartModel
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return cartModelList.size
    }

   inner class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRange: TextView = itemView!!.findViewById(R.id.tvRange)
        val tvCount: TextView = itemView!!.findViewById(R.id.tvCount)
        val deleteButton: ImageView = itemView!!.findViewById(R.id.deleteButton)
        val addButton: ImageView = itemView!!.findViewById(R.id.addButton)

        init {
            itemView.setOnClickListener {
                listener.onClickListner(adapterPosition)
            }
        }
    }
}

