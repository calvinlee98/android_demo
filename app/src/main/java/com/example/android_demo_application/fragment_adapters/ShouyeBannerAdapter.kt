package com.example.android_demo_application.fragment_adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.android_demo_application.fragments.ShouyeBannerFragment

class ShouyeBannerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return ShouyeBannerFragment()
    }

    override fun getCount() = 2
}