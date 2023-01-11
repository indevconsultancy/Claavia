package com.indev.claraa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.bumptech.glide.Glide
import com.indev.claraa.R
import com.indev.claraa.SweetDialog
import com.indev.claraa.entities.CartModel
import com.indev.claraa.entities.OrderDetailsModel
import com.indev.claraa.repository.OrderHistoryReposetory
import com.indev.claraa.repository.ProductRepository
import com.indev.claraa.restApi.ClientApi
import kotlinx.coroutines.*

class OrderHistoryHeaderAdapter(private val context: Context, var orderDetailsArrayList: ArrayList<OrderDetailsModel>, private val listener: ClickLinstener) : RecyclerView.Adapter<OrderHistoryHeaderAdapter.MyViewholder>(){

    lateinit var orderDetailsListbyId: List<OrderDetailsModel>
    lateinit var orderDetailsList: ArrayList<OrderDetailsModel>
    lateinit var adapterOrderDetailsItem: OrderHistoryItemAdapter
    lateinit var orderDetailsModel: OrderDetailsModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_order_history, parent, false)
        return MyViewholder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {

        SweetDialog.showProgressDialog(context)
        val currentItem = orderDetailsArrayList[position]

        var productIds= currentItem.product_id
       CoroutineScope(Dispatchers.IO).launch {
            var image= OrderHistoryReposetory.getImage(context, currentItem.product_id).toString()
            var product_name= OrderHistoryReposetory.getProductName(context, currentItem.product_id).toString()
            var packID= OrderHistoryReposetory.getPacksID(context, currentItem.product_id)
            var packSize= OrderHistoryReposetory.getPacksSize(context, packID.toString()).toString()
           withContext(Dispatchers.Main) {
               holder.tvProductName.text = product_name + " (" + packSize + ") "
               Glide.with(context).load(ClientApi.BASE_IMAGE_URL + image).into(holder.imageProduct)
           }

           orderDetailsList = ArrayList<OrderDetailsModel>()
           val stringArray: List<String> = productIds.split(",")
           if(stringArray.size >0) {
               for (i in stringArray) {

                   var product_id = i
                   orderDetailsListbyId = OrderHistoryReposetory.getOrderDetailsListbyID(
                       product_id,
                       context
                   ) as ArrayList<OrderDetailsModel>

                   orderDetailsModel = OrderDetailsModel(
                       orderDetailsListbyId.get(0).local_id,
                       orderDetailsListbyId.get(0).id,
                       orderDetailsListbyId.get(0).order_id,
                       orderDetailsListbyId.get(0).cart_id,
                       orderDetailsListbyId.get(0).product_id,
                       orderDetailsListbyId.get(0).price,
                       orderDetailsListbyId.get(0).quantity,
                       orderDetailsListbyId.get(0).payment_status,
                       orderDetailsListbyId.get(0).amount,
                       orderDetailsListbyId.get(0).user_id,
                       orderDetailsListbyId.get(0).active
                   )
                   orderDetailsList.add(orderDetailsModel)
               }
               adapterOrderDetailsItem =
                   OrderHistoryItemAdapter(context, orderDetailsList, listener)
               adapterOrderDetailsItem.setData(orderDetailsList as ArrayList<OrderDetailsModel>)
               showRecycleViewList(holder)
           }else{
               orderDetailsListbyId = OrderHistoryReposetory.getOrderDetailsListbyID(
                   currentItem.product_id,
                   context
               ) as ArrayList<OrderDetailsModel>
           }
           adapterOrderDetailsItem = OrderHistoryItemAdapter(context, orderDetailsListbyId, listener)
           adapterOrderDetailsItem.setData(orderDetailsListbyId as ArrayList<OrderDetailsModel>)
           showRecycleViewList(holder)
        }

        SweetDialog.dismissDialog()
    }

    private fun showRecycleViewList(holder: MyViewholder) {
        holder.rvOrderDetailsItem.apply {
            setHasFixedSize(true)
            holder.rvOrderDetailsItem.layoutManager = LinearLayoutManager(context)
            adapter= adapterOrderDetailsItem
        }
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
        val imageProduct: ImageView = itemView!!.findViewById(R.id.imageProduct)
        val rvOrderDetailsItem: RecyclerView = itemView!!.findViewById(R.id.rvOrderDetailsItem)
    }

}