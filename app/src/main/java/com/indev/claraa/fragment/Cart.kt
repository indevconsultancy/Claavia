package com.indev.claraa.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R
import com.indev.claraa.adapter.CartHeaderAdapter
import com.indev.claraa.databinding.FragmentCartBinding
import com.indev.claraa.entities.CartModel
import com.indev.claraa.repository.ProductRepository
import com.indev.claraa.viewmodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Cart : Fragment(), ClickLinstener {
    lateinit var binding: FragmentCartBinding
    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartHeaderAdapter: CartHeaderAdapter
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
        cartHeaderAdapter = CartHeaderAdapter(requireContext(),ArrayList<CartModel>(), this)
        recycleViewList()

        cartViewModel.getCartList(requireContext())?.observe(viewLifecycleOwner, Observer {
            cartHeaderAdapter.setData(it as ArrayList<CartModel>)
            cartModelList = it
            if(cartModelList.size> 0) {
                binding.llEmpty.visibility = View.GONE
                binding.llMain.visibility = View.VISIBLE
            }else{
                binding.llEmpty.visibility = View.VISIBLE
                binding.llMain.visibility = View.GONE
            }
        })

        binding.toolbar.backClick.setOnClickListener(){
            replaceFregment(Home())
        }

        binding.toolbar.toolbarTitle.text = "Cart"

    }

    override fun updateTextInteger(amount: Int) {
        binding.totalAmount.text = "SubTotal ₹\u200E" + amount
    }

    override fun updateTextString(power_range: String) {
    }

    override fun callUpdateCart(id: Int, qty: String) {
    }

    private fun recycleViewList() {
        binding.rvOrder.apply {
            setHasFixedSize(true)
            binding.rvOrder.layoutManager = LinearLayoutManager(context)
            adapter= cartHeaderAdapter
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

}