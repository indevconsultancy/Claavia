package com.indev.claraa.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.agraharisoft.notepad.Listener.ClickLinstener
import com.indev.claraa.R
import com.indev.claraa.databinding.FragmentOTPScreenBinding
import com.indev.claraa.viewmodel.OTPViewModel
import com.indev.claraa.viewmodel.OTPViewModelFactory

class OTPScreen : Fragment(), ClickLinstener {
    private lateinit var binding: FragmentOTPScreenBinding
    private lateinit var otpViewModel: OTPViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_o_t_p_screen, container, false)
        otpViewModel = ViewModelProvider(
            this,
            OTPViewModelFactory(requireContext())
        )[OTPViewModel::class.java]
        binding.otpVM = otpViewModel

        return binding.root
    }

    override fun onClickListner(position: Int) {
    }

    override fun updateTextView(amount: Int) {
    }


}