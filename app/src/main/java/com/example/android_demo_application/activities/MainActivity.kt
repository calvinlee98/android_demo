package com.example.android_demo_application.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.android_demo_application.R
import com.example.android_demo_application.fragment_adapters.MainActivityAdapter
import com.example.android_demo_application.fragments.ShouyeFragment
import com.example.android_demo_application.fragments.WodeFragment
import com.google.android.material.tabs.TabLayout
import java.util.*

class MainActivity : AppCompatActivity() {
    var pager: ViewPager? = null
    var tabLayout: TabLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        //  view的初始化
        tabLayout = findViewById(R.id.tabs)
        pager = findViewById(R.id.pager)

        //创建一个fragments list
        val list: MutableList<Fragment> = ArrayList()
        list.add(ShouyeFragment())
        list.add(ShouyeFragment())
        list.add(ShouyeFragment())
        list.add(WodeFragment())

        //主页  viewpager 的 adapter
        val adapter = MainActivityAdapter(supportFragmentManager, list)

        //pager
        pager?.adapter = adapter
        pager?.offscreenPageLimit = 3
        tabLayout?.setupWithViewPager(pager)
        initTab()
    }

    private fun initTab() {
        var tab: TabLayout.Tab? = tabLayout!!.getTabAt(0)
        tab!!.setText(R.string.shouye)
        tab.setIcon(R.drawable.shouye)
        tab = tabLayout!!.getTabAt(1)
        tab!!.setText(R.string.wenda)
        tab.setIcon(R.drawable.wenda)
        tab = tabLayout!!.getTabAt(2)
        tab!!.setText(R.string.tixi)
        tab.setIcon(R.drawable.tixi)
        tab = tabLayout!!.getTabAt(3)
        tab!!.setText(R.string.wode)
        tab.setIcon(R.drawable.wode)
    }
}