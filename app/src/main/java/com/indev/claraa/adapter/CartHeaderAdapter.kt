package com.indev.claraa.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.bumptech.glide.Glide
import com.indev.claraa.R
import com.indev.claraa.SweetDialog
import com.indev.claraa.entities.CartModel
import com.indev.claraa.entities.ProductPacketModel
import com.indev.claraa.repository.ProductRepository
import com.indev.claraa.restApi.ClientApi
import kotlinx.coroutines.*


class CartHeaderAdapter(
    val context: Context,
    var cartModelList: List<CartModel>,
    private val listener: ClickLinstener
) : RecyclerView.Adapter<CartHeaderAdapter.MyViewholder>(){

    lateinit var cartModelListbyProduct: List<CartModel>
    lateinit var adapterCart: CartItemAdapter


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cart_header_design, parent, false)
        return MyViewholder(itemView)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        SweetDialog.showProgressDialog(context)

        val currentItem = cartModelList[position]

        GlobalScope.launch {
         var  packs_size = ProductRepository.getPacksSize(currentItem.packet_id, context) as ArrayList<ProductPacketModel>
            if(currentItem.product_name.contains("Solution-") == true) {
                holder.tvProductName.setText(currentItem.product_name + " (" + packs_size.get(0).packet_size+")")
            }else{
                holder.tvProductName.setText(currentItem.product_name + " (" + packs_size.get(0).packet_size+")")
            }
        }
        Glide.with(context).load(ClientApi.BASE_IMAGE_URL +currentItem.product_img1).into(holder.imageProduct)

        CoroutineScope(Dispatchers.IO).launch {
            cartModelListbyProduct = ProductRepository.getCartDatabyProductId(
                currentItem.product_id.toInt(),
                context
            ) as ArrayList<CartModel>

            adapterCart = CartItemAdapter(context, cartModelListbyProduct, listener)
            adapterCart.setData(cartModelListbyProduct as ArrayList<CartModel>)
            showRecycleViewList(holder)
        }
        SweetDialog.dismissDialog()

    }

    private fun showRecycleViewList(holder: MyViewholder) {
        holder.rvCartItem.apply {
            setHasFixedSize(true)
            holder.rvCartItem.layoutManager = LinearLayoutManager(context)
            adapter= adapterCart
        }
    }

    fun setData(cartModel: ArrayList<CartModel>) {
        this.cartModelList= cartModel
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return cartModelList.size
    }

    inner class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProductName: TextView = itemView!!.findViewById(R.id.tvProductName)
        val imageProduct: ImageView = itemView!!.findViewById(R.id.imageProduct)
        val rvCartItem: RecyclerView = itemView!!.findViewById(R.id.rvCartItem)
        init {
            itemView.setOnClickListener {
                listener.onClickListner(adapterPosition)
            }
        }
    }

}
