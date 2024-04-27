package com.sparta.imagesearch.ui.main

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sparta.imagesearch.ui.folder.FolderFragment
import com.sparta.imagesearch.ui.search.SearchFragment

class MainPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) SearchFragment()
        else FolderFragment()
    }
}