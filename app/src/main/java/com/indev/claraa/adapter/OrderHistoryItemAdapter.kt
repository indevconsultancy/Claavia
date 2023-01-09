package com.indev.claraa.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R
import com.indev.claraa.entities.OrderDetailsModel
import com.indev.claraa.entities.ProductPacketModel
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.repository.OrderHistoryReposetory
import com.indev.claraa.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderHistoryItemAdapter (val context: Context, var orderDetailsArrayList: List<OrderDetailsModel>, private val listener: ClickLinstener) : RecyclerView.Adapter<OrderHistoryItemAdapter.MyViewholder>(){

    lateinit var prefHelper: PrefHelper
    var count =0
    var totalPrice= 0
    var check_cart_list = false
    var isTextChanged= false
    var packs_size= ArrayList<ProductPacketModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_order_details_item, parent, false)
        return MyViewholder(itemView)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: MyViewholder, position: Int) {

        val currentItem = orderDetailsArrayList[position]
        CoroutineScope(Dispatchers.IO).launch {
            var packID= OrderHistoryReposetory.getPacksID(context, currentItem.product_id)

            packs_size = ProductRepository.getPacksSize(packID.toString(), context) as ArrayList<ProductPacketModel>
            var range= OrderHistoryReposetory.getPowerSize(context, currentItem.product_id).toString()
            var product_name= OrderHistoryReposetory.getProductName(context, currentItem.product_id).toString()

            withContext(Dispatchers.Main) {
                if (product_name.contains("Solution-") == true) {
                    holder.tvRange.text = range
                    holder.tvRange.visibility = View.INVISIBLE
                } else {
                    holder.tvRange.text = range
                    holder.tvRange.visibility = View.VISIBLE
                }
            }
        }

        count= currentItem.quantity.toInt()
        totalProduct = count
        prefHelper= PrefHelper(context)
        check_cart_list=prefHelper.getBoolean(Constant.PREF_IS_CHECK_CART)

        holder.tvQuantity.text = "Quantity: " + currentItem.quantity

        holder.tvPrice.text = " "+ currentItem.price.toString()
        holder.tvtotalAmount.text = " "+ currentItem.amount.toString()
        totalAmount= grandTotal(orderDetailsArrayList)
        listener.updateTextInteger(totalAmount)
    }

    private fun grandTotal(size: List<OrderDetailsModel>): Int {
        var totalPrice = 0
        for (i in size.indices) {
            totalPrice += size[i].amount
        }
        return totalPrice
    }
    fun setData(orderDetailsArrayLists: ArrayList<OrderDetailsModel>) {
        this.orderDetailsArrayList= orderDetailsArrayLists
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return orderDetailsArrayList.size
    }

    inner class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRange: TextView = itemView!!.findViewById(R.id.tvRange)
        val tvPrice: TextView = itemView!!.findViewById(R.id.tvPrice)
        val tvtotalAmount: TextView = itemView!!.findViewById(R.id.totalAmount)
        val tvQuantity: TextView = itemView!!.findViewById(R.id.tvQuantity)
    }

    companion object{
        var totalAmount = 0
        var totalProduct = 0
    }
}
