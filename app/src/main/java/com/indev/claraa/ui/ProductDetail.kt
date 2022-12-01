package com.indev.claraa.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.indev.claraa.databinding.ActivityProductDetailBinding
import com.indev.claraa.entities.Cart
import com.indev.claraa.repository.CartRepository
import com.indev.claraa.viewmodel.ProductDetailViewModel
import com.indev.claraa.viewmodel.ProductDetailViewModelFactory

class ProductDetail : AppCompatActivity(), ClickLinstener {
    private lateinit var binding:ActivityProductDetailBinding
    private lateinit var productDetailViewModel: ProductDetailViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var powerRangeAdapter: PowerRangeAdapter
    private lateinit var cartList: ArrayList<Cart>
    val productList: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail)


        val carousel: ImageCarousel = findViewById(R.id.carousel)
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


        productDetailViewModel = ProductDetailViewModel(applicationContext)
        cartAdapter = CartAdapter(applicationContext, ArrayList<Cart>(), this)
        recycleViewList()

        productDetailViewModel.getCartList(this)?.observe(this, Observer {
            cartAdapter.setData(it as ArrayList<Cart>)
            cartList = it
        })


        productList.add("-0.5")
        productList.add("-1.0")
        productList.add("-1.5")
        productList.add("-2.0")

        productDetailViewModel = ProductDetailViewModel(applicationContext)
        powerRangeAdapter =
            PowerRangeAdapter(productDetailViewModel, applicationContext, productList, this)
        recycleViewPowerrangeList()

        productDetailViewModel.optionSelectedListener.observe(this, Observer { pair ->
            if (pair != null) {
                binding.txtRange.text = "Power Range: " + pair.first
            }else{
                binding.txtRange.text = "Power Range: " + "-0.5"
            }
        })

        binding.toolbar.menuClick.setOnClickListener(){
            onBackPressed()
        }

        binding.toolbar.home.setOnClickListener(){
            val intent = Intent(this@ProductDetail, HomeScreen::class.java)
            startActivity(intent)
        }


        var cartRepository= CartRepository()
        productDetailViewModel = ViewModelProvider(this, ProductDetailViewModelFactory(this))[ProductDetailViewModel::class.java]
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

    private fun recycleViewList() {
        binding.recyclerViewCart.apply {
            setHasFixedSize(true)
            binding.recyclerViewCart.layoutManager = LinearLayoutManager(context)
            adapter= cartAdapter
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onClickListner(position: Int) {
        Toast.makeText(applicationContext, ""+position, Toast.LENGTH_LONG).show()
    }
}