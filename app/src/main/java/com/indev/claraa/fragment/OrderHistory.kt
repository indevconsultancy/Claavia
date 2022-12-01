package com.indev.claraa.fragment

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
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
import com.indev.claraa.adapter.PowerRangeAdapter
import com.indev.claraa.databinding.FragmentOrderHistoryBinding
import com.indev.claraa.entities.Cart
import com.indev.claraa.ui.HomeScreen
import com.indev.claraa.viewmodel.*

class OrderHistory : Fragment(), ClickLinstener {
    lateinit var binding:FragmentOrderHistoryBinding
    private lateinit var orderHistoryViewModel: OrderHistoryViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartList: ArrayList<Cart>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_order_history, container, false)
        orderHistoryViewModel = ViewModelProvider(
            this,
            OrderHistoryViewModelFactory(requireContext())
        )[OrderHistoryViewModel::class.java]
        binding.orderVM = orderHistoryViewModel

        return binding.root


    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderHistoryViewModel = OrderHistoryViewModel(requireContext())
        cartAdapter = CartAdapter( requireContext(),ArrayList<Cart>(), this)
        recycleViewList()

        orderHistoryViewModel.getCartList(requireContext())?.observe(viewLifecycleOwner, Observer {
            cartAdapter.setData(it as ArrayList<Cart>)
            cartList = it
        })

        binding.toolbar.menuClick.setOnClickListener(){
            replaceFregment(Home())
        }


        binding.toolbar.home.setOnClickListener(){
            replaceFregment(Home())
        }

    }


    private fun recycleViewList() {
        binding.rvOrder.apply {
            setHasFixedSize(true)
            binding.rvOrder.layoutManager = LinearLayoutManager(context)
            adapter= cartAdapter
        }
    }
    override fun onResume() {
        super.onResume()
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { _, keyCode, event ->
            event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK
        }
    }

    private fun replaceFregment(fragment : Fragment) {
        val fragmentTransition= fragmentManager?.beginTransaction()
        fragmentTransition?.replace(R.id.frame_layout, fragment)
        fragmentTransition?.commit()
    }

    override fun onClickListner(position: Int) {
    }

}