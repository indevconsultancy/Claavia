package com.indev.claraa.fragment

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
import com.indev.claraa.databinding.FragmentCartBinding
import com.indev.claraa.entities.CartModel
import com.indev.claraa.viewmodel.*

class Cart : Fragment(), ClickLinstener {
    lateinit var binding: FragmentCartBinding
    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartModelList: ArrayList<CartModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_cart, container, false)
        cartViewModel = ViewModelProvider(
            this,
            CartViewModelFactory(requireContext())
        )[CartViewModel::class.java]
        binding.cartVM = cartViewModel

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartViewModel = CartViewModel(requireContext())
        cartAdapter = CartAdapter( requireContext(),ArrayList<CartModel>(), this)
        recycleViewList()

        cartViewModel.getCartList(requireContext())?.observe(viewLifecycleOwner, Observer {
            cartAdapter.setData(it as ArrayList<CartModel>)
            cartModelList = it
        })

        binding.toolbar.menuClick.setOnClickListener(){
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