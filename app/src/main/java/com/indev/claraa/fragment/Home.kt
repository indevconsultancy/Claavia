package com.indev.claraa.fragment

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.indev.claraa.adapter.HomeAdapter
import com.indev.claraa.databinding.FragmentHomeBinding
import com.indev.claraa.entities.HomeModel
import com.indev.claraa.ui.LoginScreen
import com.indev.claraa.viewmodel.HomeScreenViewModel
import com.indev.claraa.viewmodel.HomeScreenViewModelFactory


class Home : Fragment(), ClickLinstener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeScreenViewModel: HomeScreenViewModel
    private lateinit var homeAdapter: HomeAdapter
    var homeModelList: ArrayList<String> = ArrayList()

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

        homeModelList.add("Claraa Flesh Flo")
        homeModelList.add("Claraa Flesh Flo Along with Solution Bottle 80ml")
        homeModelList.add("Claraa Fresh Touch")
        homeModelList.add("Claraa Rainbow Fresh")
        homeScreenViewModel = HomeScreenViewModel(requireContext())
        homeAdapter = HomeAdapter( requireContext(),homeModelList, this)
        recycleViewList()

        val carousel: ImageCarousel = binding.carousel
        binding.toolbar.menuClick.setOnClickListener {
            binding.myDrawerLayout.openDrawer(GravityCompat.START)
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
                replaceFregment(Cart())
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
//    override fun onResume() {
//        super.onResume()
//        requireView().isFocusableInTouchMode = false
//        requireView().requestFocus()
//        requireView().setOnKeyListener { _, keyCode, event ->
//            event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK
//        }
//    }


    private fun recycleViewList() {
        binding.rvProductType.apply {
            setHasFixedSize(true)
            binding.rvProductType.layoutManager = LinearLayoutManager(context)
            (binding.rvProductType.layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
            adapter= homeAdapter
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
                val intent= Intent(activity, LoginScreen::class.java)
                startActivity(intent)
            }
            .showCancelButton(true)
            .setCancelClickListener { sDialog -> // Showing simple toast message to user
                sDialog.cancel()
            }.show()
    }

    override fun onClickListner(position: Int) {
        TODO("Not yet implemented")
    }


}