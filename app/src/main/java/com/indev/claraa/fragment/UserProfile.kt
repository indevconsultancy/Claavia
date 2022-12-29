package com.indev.claraa.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.indev.claraa.R
import com.indev.claraa.databinding.FragmentProfileBinding
import com.indev.claraa.helper.Constant
import com.indev.claraa.helper.PrefHelper
import com.indev.claraa.viewmodel.ProfileViewModel
import com.indev.claraa.viewmodel.ProfileViewModelFactory

class UserProfile : Fragment() {

     lateinit var binding: FragmentProfileBinding
     lateinit var profileViewModel: ProfileViewModel
     lateinit var prefHelper: PrefHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_profile, container, false)
        profileViewModel = ViewModelProvider(
            this,
            ProfileViewModelFactory(requireContext())
        )[ProfileViewModel::class.java]
        binding.profileVM = profileViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        prefHelper= PrefHelper(requireContext())
        profileViewModel.readAllData.observe(viewLifecycleOwner, Observer {

            if(it != null) {
                binding.tvName.text = it?.owner_name
                binding.tvShopName.text = it?.shop_name
                binding.tvEmail.text = it?.email
                binding.tvMobile.text = it?.mobile_number
                profileViewModel.getState(it.state_id)
                profileViewModel.getDistrict(it.district_id)
                binding.tvAddress.text = it?.address
                binding.tvPincode.text = it?.pinCode
            }
        })
        binding.tvCredit.text = prefHelper.getString(Constant.PREF_CREDIT)

            binding.toolbar.toolbarTitle.text = "Profile"

        binding.toolbar.backClick.setOnClickListener {
            replaceFregment(Home())
        }

    }

    private fun replaceFregment(fragment : Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransition= fragmentManager?.beginTransaction()
        fragmentTransition?.replace(R.id.frame_layout, fragment)
        fragmentTransition?.addToBackStack(null)
        fragmentTransition?.commit()
    }
}
