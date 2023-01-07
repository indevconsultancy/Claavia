package com.indev.claraa.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.aemerse.slider.ImageCarousel
import com.aemerse.slider.model.CarouselItem
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R
import com.indev.claraa.adapter.ProductMasterAdapter
import com.indev.claraa.adapter.SolutionAdapter
import com.indev.claraa.databinding.FragmentHomeBinding
import com.indev.claraa.entities.ProductMasterModel
import com.indev.claraa.entities.SliderModel
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.ui.LoginScreen
import com.indev.claraa.viewmodel.HomeScreenViewModel
import com.indev.claraa.viewmodel.HomeScreenViewModelFactory


class Home : Fragment(), ClickLinstener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeScreenViewModel: HomeScreenViewModel
    private lateinit var productMasterAdapter: ProductMasterAdapter
    private lateinit var solutionAdapter: SolutionAdapter
    private lateinit var sliderArrayList: ArrayList<SliderModel>
    var productMasterModelArrayList: ArrayList<ProductMasterModel> = ArrayList()
    lateinit var prefHelper: PrefHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        homeScreenViewModel = ViewModelProvider(this,
            HomeScreenViewModelFactory(requireContext())
        )[HomeScreenViewModel::class.java]
        binding.homeVM = homeScreenViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProductRecycleViewList(1)
        getProductRecycleSolutionViewList(4)
        prefHelper= PrefHelper(requireContext())

        binding.btnMonthly.setOnClickListener {
            getProductRecycleViewList(1)
            binding.btnMonthly.setBackgroundResource(R.drawable.selected_btn)
            binding.btnDaily.setBackgroundResource(R.drawable.btn_color_change)
            binding.btnWeekly.setBackgroundResource(R.drawable.btn_color_change)
            binding.btnSolution.setBackgroundResource(R.drawable.btn_color_change)

        }
        binding.btnDaily.setOnClickListener {
            getProductRecycleViewList(2)
            binding.btnMonthly.setBackgroundResource(R.drawable.btn_color_change)
            binding.btnDaily.setBackgroundResource(R.drawable.selected_btn)
            binding.btnWeekly.setBackgroundResource(R.drawable.btn_color_change)
            binding.btnSolution.setBackgroundResource(R.drawable.btn_color_change)
        }

        binding.btnWeekly.setOnClickListener {
            getProductRecycleViewList(3)
            binding.btnMonthly.setBackgroundResource(R.drawable.btn_color_change)
            binding.btnDaily.setBackgroundResource(R.drawable.btn_color_change)
            binding.btnWeekly.setBackgroundResource(R.drawable.selected_btn)
            binding.btnSolution.setBackgroundResource(R.drawable.btn_color_change)

        }

        binding.btnSolution.setOnClickListener {
            getProductRecycleViewList(4)
            binding.btnDaily.setBackgroundResource(R.drawable.btn_color_change)
            binding.btnMonthly.setBackgroundResource(R.drawable.btn_color_change)
            binding.btnWeekly.setBackgroundResource(R.drawable.btn_color_change)
            binding.btnSolution.setBackgroundResource(R.drawable.selected_btn)
        }

        val carousel: ImageCarousel = binding.carousel
        binding.toolbar.menuClick.setOnClickListener {
            binding.myDrawerLayout.openDrawer(GravityCompat.START)
        }


        carousel.registerLifecycle(lifecycle)
        val list = mutableListOf<CarouselItem>()
        val headers = mutableMapOf<String, String>()
        headers["header_key"] = "header_value"

        list.add(
            CarouselItem(
                imageUrl = "https://claraa.indevconsultancy.in/api/product_image/claraa1.jpg",
            )
        )

        list.add(
            CarouselItem(
                imageUrl = "https://claraa.indevconsultancy.in/api/product_image/claraa2.jpg"
            )
        )

        list.add(
            CarouselItem(
                imageUrl = "https://claraa.indevconsultancy.in/api/product_image/claraa3.jpg",
                headers = headers
            )
        )
        carousel.setData(list)

        var view= binding.navigationMenu.getHeaderView(0)
        var tvTitle: TextView = view.findViewById(R.id.tvTitle)
        var tvEmail: TextView = view.findViewById(R.id.tvEmail)
        tvTitle.text = prefHelper.getString(Constant.PREF_USER_NAME)
        tvEmail.text = prefHelper.getString(Constant.PREF_USER_EMAIL)

        binding.navigationMenu.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.profile ->{
                    replaceFregment(UserProfile())
                }R.id.order ->{
                replaceFregment(Cart())
            }R.id.refer ->{
                replaceFregment(Refer())
            }R.id.orderHistory ->{
                replaceFregment(OrderHistory())
            }
                R.id.logout -> {
                    logoutPopUp()
                }
            }
            binding.myDrawerLayout.closeDrawer(GravityCompat.START)
            true
        }

    }

    private fun getProductRecycleViewList(selectedCategory: Int) {
        homeScreenViewModel = HomeScreenViewModel(requireContext())
        productMasterAdapter = ProductMasterAdapter(requireContext(),productMasterModelArrayList, this)
        recycleViewList()

        homeScreenViewModel.getProductMasterList(requireActivity(),selectedCategory)?.observe(requireActivity(), Observer {
            productMasterAdapter.setData(it as ArrayList<ProductMasterModel>)
            productMasterModelArrayList = it
        })
    }

    private fun getProductRecycleSolutionViewList(selectedCategory: Int) {
        homeScreenViewModel = HomeScreenViewModel(requireContext())
        solutionAdapter = SolutionAdapter(requireContext(), productMasterModelArrayList, this)
        recycleViewSolutionList()

        homeScreenViewModel.getProductMasterList(requireActivity(), selectedCategory)
            ?.observe(requireActivity(), Observer {
                solutionAdapter.setData(it as ArrayList<ProductMasterModel>)
                productMasterModelArrayList = it
            })
    }

    private fun recycleViewSolutionList() {
        binding.rvSolution.apply {
            setHasFixedSize(true)
            binding.rvSolution.layoutManager = LinearLayoutManager(context)
            (binding.rvSolution.layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
            adapter= solutionAdapter
        }
    }

    private fun recycleViewList() {
        binding.rvProductType.apply {
            setHasFixedSize(true)
            binding.rvProductType.layoutManager = LinearLayoutManager(context)
            (binding.rvProductType.layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
            adapter= productMasterAdapter
        }
    }

    private fun replaceFregment(fragment : Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransition= fragmentManager?.beginTransaction()
        fragmentTransition?.replace(R.id.frame_layout, fragment)
        fragmentTransition?.addToBackStack(null)
        fragmentTransition?.commit()
    }

    private fun logoutPopUp() {
        SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE).setTitleText("Logout")
            .setContentText("Are you sure you want to logout?").setCancelText("Cancel")
            .setConfirmText("Ok")
            .setConfirmClickListener {
                prefHelper= PrefHelper(requireContext())
                prefHelper.put( Constant.PREF_IS_LOGIN,false)
                prefHelper.put(Constant.PREF_USERID,0)
                val intent= Intent(activity, LoginScreen::class.java)
                startActivity(intent)
            }
            .showCancelButton(true)
            .setCancelClickListener { sDialog -> // Showing simple toast message to user
                sDialog.cancel()
            }.show()
    }

    override fun onClickListner(position: Int) {

    }

    override fun updateTextInteger(amount: Int) {
    }

    override fun updateTextString(power_range: String) {
    }

    override fun callUpdateCart(id: Int, qty: String) {
    }

}