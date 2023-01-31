package com.indev.claraa.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aemerse.slider.ImageCarousel
import com.aemerse.slider.model.CarouselItem
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R
import com.indev.claraa.adapter.PowerRangeAdapter
import com.indev.claraa.adapter.ProductMasterAdapter
import com.indev.claraa.databinding.FragmentProductDetailsBinding
import com.indev.claraa.entities.*
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.restApi.ClientApi
import com.indev.claraa.viewmodel.ProductDetailViewModel
import com.indev.claraa.viewmodel.ProductDetailViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProductDetails : Fragment(), ClickLinstener {

    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var productDetailViewModel: ProductDetailViewModel
    private lateinit var powerRangeAdapter: PowerRangeAdapter
    private lateinit var cartModelList: ArrayList<CartModel>
    lateinit var productMasterArrayList: ArrayList<ProductMasterModel>
    var selectedProduct =""
    var selectedPacketID =""
    private lateinit var packsArrayList: ArrayList<ProductPacketModel>
    lateinit var prefHelper: PrefHelper


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
        prefHelper= PrefHelper(requireContext())
        binding.toolbar.backClick.setOnClickListener(){
            replaceFregment(Home())
        }
        binding.toolbar.toolbarTitle.text = "Product Details"

        productDetailViewModel = ViewModelProvider(this, ProductDetailViewModelFactory(requireActivity()))[ProductDetailViewModel::class.java]
        binding.productDetailVM = productDetailViewModel


        productDetailViewModel = ProductDetailViewModel(requireActivity())

        productDetailViewModel = ProductDetailViewModel(requireActivity())
        powerRangeAdapter = PowerRangeAdapter(requireActivity(), productMasterArrayList, this)
        recycleViewPowerrangeList()

        selectedProduct = prefHelper.getString(Constant.PREF_PRODUCT_NAME)!!
        selectedPacketID = prefHelper.getString(Constant.PREF_PACKET_ID)!!
        productDetailViewModel.getPruductPowerList(requireActivity(), selectedProduct)?.observe(requireActivity(), Observer {
            powerRangeAdapter.setData(it as ArrayList<ProductMasterModel>)
            productMasterArrayList = it
            showSlider(productMasterArrayList)
        })

        binding.tvProductName.text= prefHelper.getString(Constant.PREF_PRODUCT_NAME)


        if(prefHelper.getString(Constant.PREF_PRODUCT_NAME)?.contains("Solution-") == true){
            binding.rvPowerRange.visibility =View.GONE
            binding.cvPacket.visibility =View.GONE
        }else{
            binding.rvPowerRange.visibility =View.VISIBLE
            binding.cvPacket.visibility =View.VISIBLE
        }

        packsArrayList = ArrayList<ProductPacketModel>()
        CoroutineScope(Dispatchers.IO).launch {
            packsArrayList = productDetailViewModel.getPacksList(requireContext(), ProductMasterAdapter.packet_id) as ArrayList<ProductPacketModel>
            var productPacketModel= ProductPacketModel("0","Pack Size","0")
            packsArrayList.add(0, productPacketModel)
            val spinnerArray = arrayOfNulls<String>(packsArrayList.size)
            val spinnerMap = HashMap<Int, String>()
            for (i in 0 until packsArrayList.size) {
                spinnerMap[i] = packsArrayList.get(i).packet_id
                spinnerArray[i] = packsArrayList.get(i).packet_size
            }

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerArray)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spnLensPacket.setAdapter(adapter)
            binding.spnLensPacket.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    var id = spinnerMap[binding.spnLensPacket.getSelectedItemPosition()]
                    Log.d("TAG", "onItemSelected: " + id)
                    packet_id = id!!.toInt()

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            })
        }


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
//    override fun onResume() {
//        super.onResume()
//        requireView().isFocusableInTouchMode = true
//        requireView().requestFocus()
//        requireView().setOnKeyListener { _, keyCode, event ->
//            event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK
//        }
//    }


    private fun replaceFregment(fragment : Fragment) {
        val fragmentTransition= fragmentManager?.beginTransaction()
        fragmentTransition?.replace(R.id.frame_layout, fragment)
        fragmentTransition?.addToBackStack(null)
        fragmentTransition?.commit()
    }

    override fun onClickListner(position: Int) {
        Toast.makeText(requireActivity(), ""+position, Toast.LENGTH_LONG).show()
    }

    override fun updateTextInteger(amount: Int) {
    }

    override fun updateTextString(power_range: String) {
        binding.txtRange.text = "Power: " + power_range

    }

    override fun callUpdateCart(id: Int, qty: String) {
    }

    companion object{
        var packet_id=0
    }


}