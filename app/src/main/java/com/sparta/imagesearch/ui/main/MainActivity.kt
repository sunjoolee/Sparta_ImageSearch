package com.sparta.imagesearch.ui.main

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.sparta.imagesearch.R
import com.sparta.imagesearch.databinding.ActivityMainBinding
import com.sparta.imagesearch.databinding.TabCustomViewBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewPager()
        initTabLayout()
        customTabLayout()
    }

    private fun initViewPager(){
        binding.viewpager.adapter = MainPagerAdapter(this)
    }
    private fun initTabLayout(){
        TabLayoutMediator(binding.layoutTab, binding.viewpager) { tab, position ->
            tab.text = resources.getText(
                if (position == 0) R.string.menu_search
                else R.string.menu_folder
            )
        }.attach()
    }

    private fun customTabLayout(){
        binding.layoutTab.run {
            setCustomTabView()
            setTabSelectedListener()
            setCustomTabAnimation()
        }
    }

    private fun TabLayout.setCustomTabView() {
        TabCustomViewBinding.inflate(layoutInflater).apply {
            ivTabIcon.setImageResource(R.drawable.icon_search)
            tvTabName.setText(R.string.menu_search)

            setCustomViewColor(this.root, true)
        }.also {
            this.getTabAt(0)?.customView = it.root
        }

        TabCustomViewBinding.inflate(layoutInflater).apply {
            ivTabIcon.setImageResource(R.drawable.icon_folder)
            tvTabName.setText(R.string.menu_folder)
        }.also {
            this.getTabAt(1)?.customView = it.root
        }
    }

    private fun TabLayout.setTabSelectedListener() {
        addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                setCustomViewColor(tab?.customView!!, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                setCustomViewColor(tab?.customView!!, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun setCustomViewColor(customView: View, selected: Boolean) {
        customView.findViewById<ImageView>(R.id.iv_tab_icon)
            .imageTintList = ColorStateList.valueOf(
            resources.getColor(
                if (selected) R.color.theme_accent else R.color.gray
            )
        )
        customView.findViewById<TextView>(R.id.tv_tab_name)
            .setTextColor(
                resources.getColor(
                    if (selected) R.color.white else R.color.gray
                )
            )
    }

    private fun TabLayout.setCustomTabAnimation() {
        tabRippleColor = null
    }
}