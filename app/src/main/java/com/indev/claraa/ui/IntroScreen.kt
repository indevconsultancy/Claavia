package com.indev.claraa.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.indev.claraa.R
import com.indev.claraa.adapter.IntroSliderAdapter
import com.indev.claraa.databinding.ActivityIntroScreenBinding
import com.indev.claraa.entities.IntroSlide

class IntroScreen : AppCompatActivity() {

    lateinit var binding: ActivityIntroScreenBinding

    val introSliderAdapter = IntroSliderAdapter(
        listOf(
            IntroSlide("","",R.drawable.intro1),
            IntroSlide("","",R.drawable.intro2),
            IntroSlide("","",R.drawable.intro3),
            IntroSlide("","",R.drawable.intro4),
            IntroSlide("","",R.drawable.intro5),
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro_screen)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.introSliderViewPager.adapter = introSliderAdapter
        setupIndicators()
        setCurrentIndicator(0)
        binding.introSliderViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })

        binding.buttonNext.setOnClickListener {
            if (binding.introSliderViewPager.currentItem + 1 < introSliderAdapter.itemCount){
                binding.introSliderViewPager.currentItem += 1
            }else{
                startActivity(Intent(applicationContext, LoginScreen::class.java))
            }
        }

        binding.navigateBefore.setOnClickListener {
            if (binding.introSliderViewPager.currentItem - 1 < introSliderAdapter.itemCount){
                binding.introSliderViewPager.currentItem -= 1
            }else{
//                binding.introSliderViewPager.currentItem > -1
                finishAffinity()
            }
        }

        binding.textSkipIntro.setOnClickListener {
                startActivity(Intent(applicationContext, LoginScreen::class.java))
        }
    }

    fun setupIndicators(){
        val indicators=  arrayOfNulls<ImageView>(introSliderAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)
        for(i in indicators.indices){
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.background_border
                )
                )
                this?.layoutParams = layoutParams
            }
            binding.indicatorContainer.addView(indicators[i])
        }
    }

    fun setCurrentIndicator(index: Int){
        val childCount = binding.indicatorContainer.childCount
        for(i in 0 until childCount){
            val imageView = binding.indicatorContainer[i] as ImageView
            if(i == index){
                imageView.setImageDrawable(ContextCompat.getDrawable(
                    applicationContext,R.drawable.indicator_active
                ))
            }else{
                imageView.setImageDrawable(ContextCompat.getDrawable(
                    applicationContext,R.drawable.indicator_inactive
                ))
            }
        }
    }
}