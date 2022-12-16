package com.indev.claraa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.bumptech.glide.Glide
import com.indev.claraa.R
import com.indev.claraa.entities.CartModel
import com.indev.claraa.repository.ProductRepository
import com.indev.claraa.restApi.ClientApi

class CartAdapter(val context: Context, var cartModelList: List<CartModel>, private val listener: ClickLinstener) : RecyclerView.Adapter<CartAdapter.MyViewholder>(){
    var count =0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_cart, parent, false)
        return MyViewholder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val currentItem = cartModelList[position]
        holder.tvRange.text ="Size: " + currentItem.power_range
        holder.tvProductName.text = currentItem.product_name
        var totalPrice= currentItem.quantity.toInt() * currentItem.price.toInt()
        holder.tvPrice.text =totalPrice.toString()

        count= currentItem.quantity.toInt()

        holder.addButton.setOnClickListener {
            count= currentItem.quantity.toInt()

            if(count >0){
                Toast.makeText(context, "" + count, Toast.LENGTH_LONG).show()
                increamentCount()
                ProductRepository.updateCartProductQuantity(count,currentItem.id,context)
                holder.deleteButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_remove_24));
            }else{
                holder.deleteButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_outline_24));
            }
        }
        Glide.with(context).load(ClientApi.BASE_IMAGE_URL +currentItem.product_img1).into(holder.imageProduct)


        holder.btnDelete.setOnClickListener{
            ProductRepository.deleteProductData(currentItem.id,context)
            notifyDataSetChanged()
        }

        holder.deleteButton.setOnClickListener {
            count= currentItem.quantity.toInt()

            if(count > 1){
                decreamentCount()
                ProductRepository.updateCartProductQuantity(count,currentItem.id,context)
                holder.deleteButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_remove_24));
            }else{
                ProductRepository.deleteProductData(currentItem.id,context)
                holder.deleteButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_outline_24));
            }
        }


        if(count<=1){
            holder.deleteButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_outline_24));
        }else{
            holder.deleteButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_remove_24));
        }
        holder.tvCount.setText("" + count)

        var totalCartPrice  = totalPrice * cartModelList.size
        totalAmount= totalCartPrice
        if(count==0) {
            listener.updateTextView(0)
        }else{
            listener.updateTextView(totalAmount)
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
        val tvPrice: TextView = itemView!!.findViewById(R.id.tvPrice)
        val tvProductName: TextView = itemView!!.findViewById(R.id.tvProductName)
        val deleteButton: ImageView = itemView!!.findViewById(R.id.deleteButton)
        val imageProduct: ImageView = itemView!!.findViewById(R.id.imageProduct)
        val btnDelete: Button = itemView!!.findViewById(R.id.btnDelete)
        val addButton: ImageView = itemView!!.findViewById(R.id.addButton)

        init {
            itemView.setOnClickListener {
                listener.onClickListner(adapterPosition)
            }
        }
    }

    companion object{
        var totalAmount = 0
    }


}

