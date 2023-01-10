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
import com.indev.claraa.adapter.OrderHistoryHeaderAdapter
import com.indev.claraa.adapter.OrderListAdapter
import com.indev.claraa.databinding.FragmentOrderHistoryBinding
import com.indev.claraa.entities.OrderDetailsModel
import com.indev.claraa.entities.OrderMasterModel
import com.indev.claraa.viewmodel.*

class OrderHistory : Fragment(), ClickLinstener {
    private lateinit var binding: FragmentOrderHistoryBinding
    private lateinit var orderHistoryViewModel: OrderHistoryViewModel
    private lateinit var orderHistoryHeaderAdapter: OrderHistoryHeaderAdapter
    private lateinit var orderDetailsArrayList: ArrayList<OrderDetailsModel>
    private lateinit var orderListAdapter: OrderListAdapter
    private lateinit var orderMasterArrayList: ArrayList<OrderMasterModel>

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

        orderListAdapter = OrderListAdapter(requireContext(),ArrayList<OrderMasterModel>(), this)
        recycleViewOrderList()

        orderHistoryViewModel.getOrderList(requireActivity())?.observe(requireActivity(), Observer {
            orderListAdapter.setData(it as ArrayList<OrderMasterModel>)
            orderMasterArrayList = it
            if(orderMasterArrayList.size > 0){
                binding.llEmpty.visibility =View.GONE
                binding.llMain.visibility =View.GONE
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

    fun showOrderDetailsList(order_id: Int) {
        binding.llOrderList.visibility = View.GONE
        binding.llMain.visibility = View.VISIBLE
        orderHistoryHeaderAdapter = OrderHistoryHeaderAdapter(requireContext(),ArrayList<OrderDetailsModel>(), this)
        recycleViewList()

        orderHistoryViewModel.getOrderDetailsList(requireActivity(), order_id)?.observe(requireActivity(), Observer {
            orderHistoryHeaderAdapter.setData(it as ArrayList<OrderDetailsModel>)
            orderDetailsArrayList = it
        })
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
            adapter= orderHistoryHeaderAdapter
        }
    }
    private fun recycleViewOrderList() {
        binding.rvOrderList.apply {
            setHasFixedSize(true)
            binding.rvOrderList.layoutManager = LinearLayoutManager(context)
            adapter= orderListAdapter
        }
    }

    override fun onClickListner(order_id: Int) {
        showOrderDetailsList(order_id)
    }

    override fun updateTextInteger(amount: Int) {
    }

    override fun updateTextString(amount: String) {
        binding.totalAmount.text = "Total Amount: " + amount
    }

    override fun callUpdateCart(id: Int, qty: String) {
    }
}