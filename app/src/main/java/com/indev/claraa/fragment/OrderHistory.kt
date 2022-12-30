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
import com.indev.claraa.adapter.OrderHistoryAdapter
import com.indev.claraa.databinding.FragmentOrderHistoryBinding
import com.indev.claraa.entities.OrderDetailsModel
import com.indev.claraa.entities.OrderMasterModel
import com.indev.claraa.viewmodel.*

class OrderHistory : Fragment(), ClickLinstener {
    private lateinit var binding: FragmentOrderHistoryBinding
    private lateinit var orderHistoryViewModel: OrderHistoryViewModel
    private lateinit var orderHistoryAdapter: OrderHistoryAdapter
    private lateinit var orderDetailsArrayList: ArrayList<OrderDetailsModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_history, container, false)
        orderHistoryViewModel = ViewModelProvider(this,
            OrderHistoryFactory(requireContext())
        )[OrderHistoryViewModel::class.java]
        binding.orderHistoryVM = orderHistoryViewModel
        return binding.root
        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderHistoryViewModel = OrderHistoryViewModel(requireContext())
        orderHistoryAdapter = OrderHistoryAdapter(requireContext(),ArrayList<OrderDetailsModel>(), this)
        recycleViewList()

        orderHistoryViewModel.getOrderDetailsList(requireActivity())?.observe(requireActivity(), Observer {
            orderHistoryAdapter.setData(it as ArrayList<OrderDetailsModel>)
            orderDetailsArrayList = it
            if(orderDetailsArrayList.size > 0){
                binding.llEmpty.visibility =View.GONE
                binding.llMain.visibility =View.VISIBLE
            }else{
                binding.llEmpty.visibility =View.VISIBLE
                binding.llMain.visibility =View.GONE
            }
        })

        binding.toolbar.backClick.setOnClickListener(){
            replaceFregment(Home())
        }

        binding.toolbar.toolbarTitle.text = "Order History"


    }

    private fun replaceFregment(fragment : Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransition= fragmentManager?.beginTransaction()
        fragmentTransition?.replace(R.id.frame_layout, fragment)
        fragmentTransition?.addToBackStack(null)
        fragmentTransition?.commit()
    }

    private fun recycleViewList() {
        binding.rvOrder.apply {
            setHasFixedSize(true)
            binding.rvOrder.layoutManager = LinearLayoutManager(context)
            adapter= orderHistoryAdapter
        }
    }

    override fun onClickListner(position: Int) {
    }

    override fun updateTextView(amount: Int) {
    }

    override fun updatePowerRange(power_range: String) {
    }

    override fun callUpdateCart(id: Int, qty: String) {

    }


}