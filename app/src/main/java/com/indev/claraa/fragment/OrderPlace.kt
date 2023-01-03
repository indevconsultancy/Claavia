package com.indev.claraa.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R
import com.indev.claraa.adapter.CartAdapter
import com.indev.claraa.databinding.FragmentOrderPlaceBinding
import com.indev.claraa.entities.CartModel
import com.indev.claraa.viewmodel.*


class OrderPlace : Fragment(), ClickLinstener{
    private lateinit var binding: FragmentOrderPlaceBinding
    private lateinit var orderPlaceViewModel: OrderPlaceViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartModelList: ArrayList<CartModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_order_place, container, false)
        orderPlaceViewModel = ViewModelProvider(
            this,
            OrderPlaceViewModelFactory(requireContext())
        )[OrderPlaceViewModel::class.java]
        binding.orderPlaceVM = orderPlaceViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderPlaceViewModel= OrderPlaceViewModel(requireContext())
        cartAdapter = CartAdapter( requireContext(),ArrayList<CartModel>(), this)
        recycleViewList()

        orderPlaceViewModel.getCartList(requireContext())?.observe(viewLifecycleOwner, Observer {
            cartAdapter.setData(it as ArrayList<CartModel>)
            cartModelList = it
        })

        orderPlaceViewModel.readAllData.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvAddress.text = it.shop_name + "," + it.address1 + "," +
                        it.address2
            }
        }
        binding.toolbar.backClick.setOnClickListener(){
            replaceFregment(AddressList())
        }

        binding.toolbar.toolbarTitle.text = "Order Place"
}


    override fun updateTextInteger(amount: Int) {
        binding.tvOrderTotal.text = "₹\u200E" + amount
    }

    private fun recycleViewList() {
        binding.rvOrder.apply {
            setHasFixedSize(true)
            binding.rvOrder.layoutManager = LinearLayoutManager(context)
            adapter= cartAdapter
        }
    }

    private fun replaceFregment(fragment : Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransition= fragmentManager?.beginTransaction()
        fragmentTransition?.replace(R.id.frame_layout, fragment)
        fragmentTransition?.addToBackStack(null)
        fragmentTransition?.commit()
    }

    override fun onClickListner(position: Int) {
    }

    override fun updateTextString(power_range: String) {
    }

    override fun callUpdateCart(id: Int, qty: String) {
    }

}