package com.indev.claraa.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.bumptech.glide.Glide
import com.indev.claraa.R
import com.indev.claraa.SweetDialog
import com.indev.claraa.entities.CartModel
import com.indev.claraa.entities.ProductPacketModel
import com.indev.claraa.entities.deleteModel
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.ProductRepository
import com.indev.claraa.restApi.ClientApi
import kotlinx.coroutines.*


class CartItemAdapter(val context: Context, var cartModelList: List<CartModel>, private val listener: ClickLinstener) : RecyclerView.Adapter<CartItemAdapter.MyViewholder>(){

    lateinit var prefHelper: PrefHelper
    var count =0
    var totalPrice= 0
    var check_cart_list = false
    var isTextChanged= false
    var packs_size= ArrayList<ProductPacketModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_cart_list, parent, false)
        return MyViewholder(itemView)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: MyViewholder, position: Int) {

        val currentItem = cartModelList[position]
        CoroutineScope(Dispatchers.IO).launch {
            packs_size = ProductRepository.getPacksSize(currentItem.packet_id, context) as ArrayList<ProductPacketModel>

            withContext(Dispatchers.Main) {
                if (currentItem.product_name.contains("Solution-") == true) {
                    holder.tvRange.text = currentItem.power_range
                    holder.tvRange.visibility = View.INVISIBLE
                } else {
                    holder.tvRange.text = currentItem.power_range
                    holder.tvRange.visibility = View.VISIBLE
                }
            }
        }

        count= currentItem.quantity.toInt()
        totalProduct = count
        prefHelper= PrefHelper(context)
        check_cart_list=prefHelper.getBoolean(Constant.PREF_IS_CHECK_CART)

        if(check_cart_list == true){
            holder.llButton.visibility =View.GONE
            holder.etQuantity.visibility =View.GONE
            holder.tvQuantity.visibility= View.VISIBLE
        }else{
            holder.llButton.visibility =View.VISIBLE
            holder.tvQuantity.visibility= View.GONE
            holder.etQuantity.visibility= View.VISIBLE
        }
        holder.tvQuantity.text = "Quantity: " + currentItem.quantity

        holder.etQuantity.setText(count.toString())
        holder.btnDelete.setOnClickListener{
            deletePopupShow(currentItem.id)
            notifyDataSetChanged()
        }

        holder.btnUpdate.setOnClickListener{
            var qty= holder.etQuantity.text.toString()
            if(qty.toInt() <0 || qty.toInt() > 10000){
                Toast.makeText(context, "Quantity should be between 1- 10000", Toast.LENGTH_LONG).show()
            }else{
                SweetDialog.showProgressDialog(context)
                totalPrice= currentItem.price.toInt() * qty.toInt()
                    if(qty.toInt() > 0) {
                        ProductRepository.updateCartProductQuantity(
                            qty.toInt(),
                            totalPrice,
                            currentItem.local_id,
                            context
                        )
                        showAlertDialog()
                        notifyDataSetChanged()
                 }
                listener.callUpdateCart(currentItem.id.toInt(),qty.toInt().toString())
                SweetDialog.dismissDialog()
            }

        }

        holder.tvPrice.text = currentItem.currency +" "+ currentItem.price.toString()
        holder.tvtotalAmount.text = currentItem.currency +" "+ currentItem.amount.toString()

    }

    private fun showAlertDialog() {
        SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE)
            .setContentText("Product Updated successfully in Cart")
            .setConfirmText("Ok")
            .setConfirmClickListener {sdialog ->
                sdialog.dismiss()
            }
            .show()
    }

    private fun deletePopupShow(id: String) {
        SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE).setTitleText("")
            .setContentText("Are you sure you want to delete the cart?").setCancelText("Cancel")
            .setConfirmText("Ok")
            .setConfirmClickListener { sDialog ->
                listener.updateTextInteger(0)
                deleteCartProduct(id)
                sDialog.dismiss()
            }
            .showCancelButton(true)
            .setCancelClickListener { sDialog -> // Showing simple toast message to user
                sDialog.cancel()
            }.show()
    }

    private fun deleteCartProduct(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            ProductRepository.deleteProductData(id,context)
            var deleteModel= deleteModel(id)

            var last_id=0
            last_id = ProductRepository.cartProductDelete(context,deleteModel)
            if (last_id> 0) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "Successfully Deleted...", Toast.LENGTH_LONG).show()
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
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
        val tvRange: TextView = itemView!!.findViewById(R.id.tvRange)
        val tvCount: TextView = itemView!!.findViewById(R.id.tvCount)
        val tvPrice: TextView = itemView!!.findViewById(R.id.tvPrice)
        val tvtotalAmount: TextView = itemView!!.findViewById(R.id.totalAmount)
        val tvQuantity: TextView = itemView!!.findViewById(R.id.tvQuantity)
        val deleteButton: ImageView = itemView!!.findViewById(R.id.deleteButton)
        val btnDelete: ImageView = itemView!!.findViewById(R.id.btnDelete)
        val addButton: ImageView = itemView!!.findViewById(R.id.addButton)
        val llButton: LinearLayout = itemView!!.findViewById(R.id.llButton)
        val etQuantity: EditText = itemView!!.findViewById(R.id.etQuantity)
        val btnUpdate: ImageView = itemView!!.findViewById(R.id.btnUpdate)


        init {
            itemView.setOnClickListener {
                listener.onClickListner(adapterPosition)
            }
        }


    }

    companion object{
        var totalAmount = 0
        var totalProduct = 0
    }


}

