package com.indev.claraa.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.aemerse.slider.ImageCarousel
import com.aemerse.slider.model.CarouselItem
import com.indev.claraa.R
import com.indev.claraa.databinding.FragmentHomeBinding
import com.indev.claraa.ui.LoginScreen
import com.indev.claraa.ui.ProductDetail
import com.indev.claraa.viewmodel.HomeScreenViewModel
import com.indev.claraa.viewmodel.HomeScreenViewModelFactory


class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeScreenViewModel: HomeScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_home, container, false)
        homeScreenViewModel = ViewModelProvider(
            this,
            HomeScreenViewModelFactory(requireContext())
        )[HomeScreenViewModel::class.java]
        binding.homeVM = homeScreenViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val carousel: ImageCarousel = binding.carousel
        binding.toolbar.menuClick.setOnClickListener {
            binding.myDrawerLayout.openDrawer(GravityCompat.START)
        }

        binding.cardView.setOnClickListener {
            val intent= Intent(activity, ProductDetail::class.java)
            startActivity(intent)
        }

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


        binding.navigationMenu.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.profile ->{
                    replaceFregment(Profile())
                }R.id.order ->{
                replaceFregment(OrderHistory())
            }R.id.refer ->{
                replaceFregment(Refer())
            }
                R.id.logout -> {
                    logoutPopUp()
                }
            }
            binding.myDrawerLayout.closeDrawer(GravityCompat.START)
            true
        }

    }


    private fun replaceFregment(fragment : Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransition= fragmentManager?.beginTransaction()
        fragmentTransition?.replace(R.id.frame_layout, fragment)
        fragmentTransition?.commit()
    }

    private fun logoutPopUp() {
        SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE).setTitleText("Logout")
            .setContentText("Are you sure you want to logout?").setCancelText("Cancel")
            .setConfirmText("Ok")
            .setConfirmClickListener {
                val intent= Intent(activity, LoginScreen::class.java)
                startActivity(intent)
            }
            .showCancelButton(true)
            .setCancelClickListener { sDialog -> // Showing simple toast message to user
                sDialog.cancel()
            }.show()
    }



}