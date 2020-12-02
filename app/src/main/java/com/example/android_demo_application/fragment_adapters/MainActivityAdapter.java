package com.example.android_demo_application.fragment_adapters;

/*
* author by @李方正
* main activity  viewpager 的adapter
* */
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;


import com.example.android_demo_application.fragments.ShouyeFragment;
import com.example.android_demo_application.fragments.WodeFragment;

import java.util.List;

public class MainActivityAdapter extends FragmentPagerAdapter {
    FragmentManager fragmentManager;
    List<Fragment>list;
    public MainActivityAdapter(FragmentManager fm,List<Fragment>l){
        super(fm);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0)
            return new ShouyeFragment();
        else if(position==3)
            return new WodeFragment();
        else
            return new ShouyeFragment();
    }

}
