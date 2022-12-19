package com.indev.claraa.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
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
import com.indev.claraa.entities.ProductMasterModel
import com.indev.claraa.restApi.ClientApi
import com.indev.claraa.viewmodel.ProductDetailViewModel
import com.indev.claraa.viewmodel.ProductDetailViewModelFactory


class ProductDetails : Fragment(), ClickLinstener {

    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var productDetailViewModel: ProductDetailViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var powerRangeAdapter: PowerRangeAdapter
    private lateinit var cartModelList: ArrayList<CartModel>
    lateinit var productMasterArrayList: ArrayList<ProductMasterModel>
     var selectedProduct ="Claraa Fresh Flo"

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
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productMasterArrayList= ArrayList<ProductMasterModel>()

        binding.toolbar.backClick.setOnClickListener(){
            replaceFregment(Home())
        }
        binding.toolbar.toolbarTitle.text = "Product Details"

        productDetailViewModel = ViewModelProvider(this, ProductDetailViewModelFactory(requireActivity()))[ProductDetailViewModel::class.java]
        binding.productDetailVM = productDetailViewModel


        productDetailViewModel = ProductDetailViewModel(requireActivity())
        cartAdapter = CartAdapter(requireActivity(), ArrayList<CartModel>(), this)
        recycleViewList()

        productDetailViewModel.getCartList(requireActivity())?.observe(requireActivity(), Observer {
            cartAdapter.setData(it as ArrayList<CartModel>)
            cartModelList = it
        })

        productDetailViewModel = ProductDetailViewModel(requireActivity())
        powerRangeAdapter = PowerRangeAdapter(requireActivity(), productMasterArrayList, this)
        recycleViewPowerrangeList()

        productDetailViewModel.getPruductPowerList(requireActivity(), selectedProduct)?.observe(requireActivity(), Observer {
            powerRangeAdapter.setData(it as ArrayList<ProductMasterModel>)
            productMasterArrayList = it
            showSlider(productMasterArrayList)
        })


    }

    private fun showSlider(productMasterArrayList: ArrayList<ProductMasterModel>) {
        val carousel: ImageCarousel = binding.carousel
        carousel.registerLifecycle(lifecycle)
        val list = mutableListOf<CarouselItem>()
        val headers = mutableMapOf<String, String>()
        headers["header_key"] = "header_value"

        list.add(
            CarouselItem(
                imageUrl = ClientApi.BASE_IMAGE_URL+productMasterArrayList[0].product_img1,
            )
        )

        list.add(
            CarouselItem(
                imageUrl = ClientApi.BASE_IMAGE_URL+productMasterArrayList[0].product_img2,
                headers = headers
            )
        )
        carousel.setData(list)
    }


    private fun recycleViewPowerrangeList() {
        binding.rvPowerRange.apply {
            setHasFixedSize(true)
            binding.rvPowerRange.layoutManager = LinearLayoutManager(context)
            (binding.rvPowerRange.layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
            adapter= powerRangeAdapter
        }
    }

    private fun replaceFregment(fragment : Fragment) {
        val fragmentTransition= fragmentManager?.beginTransaction()
        fragmentTransition?.replace(R.id.frame_layout, fragment)
        fragmentTransition?.addToBackStack(null)
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

    override fun updateTextView(amount: Int) {
    }

    override fun updatePowerRange(power_range: String) {
        binding.txtRange.text = "Power Range: " + power_range

    }


}