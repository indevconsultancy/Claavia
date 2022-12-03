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
import com.indev.claraa.adapter.AddressDetailsAdapter
import com.indev.claraa.adapter.HomeAdapter
import com.indev.claraa.databinding.FragmentAddressListBinding
import com.indev.claraa.entities.AddressDetailsModel
import com.indev.claraa.entities.CartModel
import com.indev.claraa.viewmodel.*

class AddressList : Fragment(), ClickLinstener {
    private lateinit var binding: FragmentAddressListBinding
    private lateinit var addressListViewModel: AddressListViewModel
    private lateinit var addressDetailsAdapter: AddressDetailsAdapter
    var addressDetailsViewModelList: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_address_list, container, false)
        addressListViewModel = ViewModelProvider(
            this,
            AddressListViewModelFactory(requireContext())
        )[AddressListViewModel::class.java]
        binding.addressListVM = addressListViewModel

        return binding.root
        // Inflate the layout for this fragment
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        addressListViewModel = AddressListViewModel(requireContext())
//        addressDetailsAdapter = AddressDetailsAdapter(requireContext(), ,ArrayList<AddressDetailsModel>(), this)
//        recycleViewList()
//        addressListViewModel.getA(requireContext())?.observe(viewLifecycleOwner, Observer {
//            addressDetailsAdapter.setData(it as ArrayList<AddressDetailsModel>)
//            addressDetailsViewModelList = it
//        })

    }


    private fun recycleViewList() {
        binding.rvAddressList.apply {
            setHasFixedSize(true)
            binding.rvAddressList.layoutManager = LinearLayoutManager(context)
            (binding.rvAddressList.layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
            adapter= addressDetailsAdapter
        }
    }

    override fun onClickListner(position: Int) {
        TODO("Not yet implemented")
    }

}