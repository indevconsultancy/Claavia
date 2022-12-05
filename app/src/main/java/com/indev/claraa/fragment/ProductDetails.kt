package com.indev.claraa.fragment

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aemerse.slider.ImageCarousel
import com.aemerse.slider.model.CarouselItem
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R
import com.indev.claraa.adapter.CartAdapter
import com.indev.claraa.adapter.PowerRangeAdapter
import com.indev.claraa.databinding.FragmentProductDetailsBinding
import com.indev.claraa.entities.CartModel
import com.indev.claraa.repository.CartRepository
import com.indev.claraa.ui.HomeScreen
import com.indev.claraa.viewmodel.ProductDetailViewModel
import com.indev.claraa.viewmodel.ProductDetailViewModelFactory


class ProductDetails : Fragment(), ClickLinstener {

    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var productDetailViewModel: ProductDetailViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var powerRangeAdapter: PowerRangeAdapter
    private lateinit var cartModelList: ArrayList<CartModel>
    val productList: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_product_details, container, false)
        productDetailViewModel = ViewModelProvider(
            this,
            ProductDetailViewModelFactory(requireContext())
        )[ProductDetailViewModel::class.java]
        binding.productDetailVM = productDetailViewModel

        return binding.root
        return inflater.inflate(R.layout.fragment_product_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val carousel: ImageCarousel = binding.carousel
        carousel.registerLifecycle(lifecycle)

        val list = mutableListOf<CarouselItem>()

        list.add(
            CarouselItem(
                imageUrl = "https://claraa.in/images/banner/1.jpg",
            )
        )
        list.add(
            CarouselItem(
                imageUrl = "https://claraa.in/images/banner/2.jpg"
            )
        )

        val headers = mutableMapOf<String, String>()
        headers["header_key"] = "header_value"

        list.add(
            CarouselItem(
                imageUrl = "https://claraa.in/images/about/9.jpg",
                headers = headers
            )
        )
        carousel.setData(list)


        productDetailViewModel = ProductDetailViewModel(requireActivity())
        cartAdapter = CartAdapter(requireActivity(), ArrayList<CartModel>(), this)
        recycleViewList()

        productDetailViewModel.getCartList(requireActivity())?.observe(requireActivity(), Observer {
            cartAdapter.setData(it as ArrayList<CartModel>)
            cartModelList = it
        })


        productList.add("-0.5")
        productList.add("-1.0")
        productList.add("-1.5")
        productList.add("-2.0")

        productDetailViewModel = ProductDetailViewModel(requireActivity())
        powerRangeAdapter =
            PowerRangeAdapter(productDetailViewModel, requireActivity(), productList, this)
        recycleViewPowerrangeList()

        productDetailViewModel.optionSelectedListener.observe(requireActivity(), Observer { pair ->
            if (pair != null) {
                binding.txtRange.text = "Power Range: " + pair.first
            }else{
                binding.txtRange.text = "Power Range: " + "-0.5"
            }
        })

        binding.toolbar.menuClick.setOnClickListener(){
            replaceFregment(Home())
        }

//        binding.toolbar.home.setOnClickListener(){
//            replaceFregment(Home())
//        }


        var cartRepository= CartRepository()
        productDetailViewModel = ViewModelProvider(this, ProductDetailViewModelFactory(requireActivity()))[ProductDetailViewModel::class.java]
        binding.productDetailVM = productDetailViewModel
    }

    private fun recycleViewPowerrangeList() {
        binding.rvPowerRange.apply {
            setHasFixedSize(true)
            binding.rvPowerRange.layoutManager = LinearLayoutManager(context)
            (binding.rvPowerRange.layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
            adapter= powerRangeAdapter
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
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransition= fragmentManager?.beginTransaction()
        fragmentTransition?.replace(R.id.frame_layout, fragment)
        fragmentTransition?.commit()
    }

    private fun recycleViewList() {
        binding.recyclerViewCart.apply {
            setHasFixedSize(true)
            binding.recyclerViewCart.layoutManager = LinearLayoutManager(context)
            adapter= cartAdapter
        }
    }


    override fun onClickListner(position: Int) {
        Toast.makeText(requireActivity(), ""+position, Toast.LENGTH_LONG).show()
    }

}