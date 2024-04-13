package com.sparta.imagesearch.view.main

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sparta.imagesearch.view.folder.FolderFragment
import com.sparta.imagesearch.view.search.SearchFragment

class MainPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) SearchFragment()
        else FolderFragment()
    }
}