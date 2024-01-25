package com.sparta.imagesearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.sparta.imagesearch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewpager.adapter = MyPagerAdapter(this)
        TabLayoutMediator(binding.layoutTab, binding.viewpager){tab, position ->
            tab.text = resources.getText(
                if(position==0)R.string.menu_search
                else R.string.menu_folder
            )
        }.attach()
    }

    class MyPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 2
        override fun createFragment(position: Int): Fragment {
            return if(position==0) SearchFragment() else FolderFragment()
        }

    }

}