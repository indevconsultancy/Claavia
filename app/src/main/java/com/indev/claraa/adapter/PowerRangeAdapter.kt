package com.indev.claraa.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R
import com.indev.claraa.viewmodel.ProductDetailViewModel

class PowerRangeAdapter(private val productDetailViewModel: ProductDetailViewModel,private val context: Context, var productList: ArrayList<String>, private val listener: ClickLinstener) : RecyclerView.Adapter<PowerRangeAdapter.MyViewholder>(){

    var selectedItemPosition: Int = 0
    lateinit var selectValue: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_power_range, parent, false)
        return MyViewholder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val currentItem = productList[position]
        holder.tvPowerRange.text = currentItem

        holder.cardView.setOnClickListener {
            selectedItemPosition= position
            selectValue= currentItem
            clickEvent(currentItem, "1")
            notifyDataSetChanged()
        }

        if(selectedItemPosition == position){
            holder.tvPowerRange.setBackgroundColor(Color.parseColor("#E7ACB3"))
            holder.llMain.setBackgroundColor(Color.parseColor("#FFCD293C"))
        }else{
            holder.tvPowerRange.setTextColor(Color.parseColor("#FF000000"))
            holder.tvPowerRange.setBackgroundColor(Color.parseColor("#EBF3F4"))
            holder.llMain.setBackgroundColor(Color.parseColor("#4CA7BC"))
        }

    }

    private fun clickEvent(range: String, option: String) {
        productDetailViewModel.clickRangeOptionEvent(Pair(range, option))
        notifyDataSetChanged()
    }

//    fun setData(productMaster: ArrayList<ProductMasterModel>) {
//        this.productList= productMaster
//        notifyDataSetChanged()
//    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPowerRange: TextView = itemView!!.findViewById(R.id.tvPowerRange)
        val cardView: CardView = itemView!!.findViewById(R.id.cardView)
        val llMain: LinearLayout = itemView!!.findViewById(R.id.llMain)

        init {
            itemView.setOnClickListener {
                listener.onClickListner(adapterPosition)
            }
        }
    }


}
