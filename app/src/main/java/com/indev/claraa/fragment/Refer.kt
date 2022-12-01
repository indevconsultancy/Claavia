package com.indev.claraa.fragment

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.indev.claraa.R
import com.indev.claraa.databinding.FragmentProfileBinding
import com.indev.claraa.databinding.FragmentReferBinding

class Refer : Fragment() {
    private lateinit var binding: FragmentReferBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_refer,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.menuClick.setOnClickListener(){
            replaceFregment(Home())
        }


        binding.toolbar.home.setOnClickListener(){
            replaceFregment(Home())
        }

    }

    private fun replaceFregment(fragment : Fragment) {
        val fragmentTransition= fragmentManager?.beginTransaction()
        fragmentTransition?.replace(R.id.frame_layout, fragment)
        fragmentTransition?.commit()
    }

    override fun onResume() {
        super.onResume()
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { _, keyCode, event ->
            event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK
        }
    }


}