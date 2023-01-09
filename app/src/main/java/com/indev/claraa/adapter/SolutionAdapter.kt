package com.indev.claraa.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.bumptech.glide.Glide
import com.indev.claraa.R
import com.indev.claraa.entities.ProductMasterModel
import com.indev.claraa.fragment.ProductDetails
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.restApi.ClientApi
import com.indev.claraa.ui.HomeScreen

class SolutionAdapter(private val context: Context, var productMasterModelArrayList: ArrayList<ProductMasterModel>, private val listener: ClickLinstener) : RecyclerView.Adapter<SolutionAdapter.MyViewholder>(){

    lateinit var prefHelper: PrefHelper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_fragment_home, parent, false)
        return MyViewholder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        prefHelper= PrefHelper(context)
        val currentItem = productMasterModelArrayList[position]
        holder.tvProductName.text = currentItem.product_name
        holder.tvPrice.text = currentItem.currency + " " +currentItem.price
        Glide.with(context).load(ClientApi.BASE_IMAGE_URL +currentItem.product_img1).into(holder.imageProduct)
        holder.cardView.setOnClickListener{
            replaceFregment(ProductDetails())
            packet_id= currentItem.packet_id
            prefHelper.put(Constant.PREF_PRODUCT_NAME, currentItem.product_name)
            prefHelper.put(Constant.PREF_PRODUCT_ID, currentItem.product_id)
        }
    }

    private fun replaceFregment(fragment : Fragment) {
        var transaction = (context as HomeScreen).supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }

    fun setData(productMasterModel: ArrayList<ProductMasterModel>) {
        this.productMasterModelArrayList= productMasterModel
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return productMasterModelArrayList.size
    }

    inner class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView!!.findViewById(R.id.cardView)
        val imageProduct: ImageView = itemView!!.findViewById(R.id.imageProduct)
        val tvProductName: TextView = itemView!!.findViewById(R.id.tvProductName)
        val tvPrice: TextView = itemView!!.findViewById(R.id.tvPrice)

        init {
            itemView.setOnClickListener {
                listener.onClickListner(adapterPosition)
            }
        }
    }

    companion object{
        var packet_id= "1"
    }

}
