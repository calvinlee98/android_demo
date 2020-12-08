package com.example.android_demo_application.fragment_adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/*
* author by @李方正
* main activity  viewpager 的adapter
* */   class MainActivityAdapter(fm: FragmentManager?, var list: List<Fragment>) : FragmentPagerAdapter(fm!!) {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

}