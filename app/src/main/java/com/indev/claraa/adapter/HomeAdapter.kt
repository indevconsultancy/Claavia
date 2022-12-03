package com.indev.claraa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R
import com.indev.claraa.entities.HomeModel
import com.indev.claraa.fragment.ProductDetails
import com.indev.claraa.ui.HomeScreen

class HomeAdapter(private val context: Context, var homeModelList: ArrayList<String>, private val listener: ClickLinstener) : RecyclerView.Adapter<HomeAdapter.MyViewholder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_fragment_home, parent, false)
        return MyViewholder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val currentItem = homeModelList[position]
        holder.tvProductName.text = currentItem
        holder.cardView.setOnClickListener{
            replaceFregment(ProductDetails())
        }
    }

    private fun replaceFregment(fragment : Fragment) {
        var transaction = (context as HomeScreen).supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }

    fun setData(homeModel: ArrayList<String>) {
        this.homeModelList= homeModel
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return homeModelList.size
    }

    inner class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView!!.findViewById(R.id.cardView)
        val tvProductName: TextView = itemView!!.findViewById(R.id.tvProductName)

        init {
            itemView.setOnClickListener {
                listener.onClickListner(adapterPosition)
            }
        }
    }


}
