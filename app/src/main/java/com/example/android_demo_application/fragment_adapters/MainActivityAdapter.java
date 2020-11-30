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



import java.util.List;

public class MainActivityAdapter extends FragmentPagerAdapter {
    FragmentManager fragmentManager;
    List<Fragment>list;
    public MainActivityAdapter(FragmentManager fm,List<Fragment>l){
        super(fm);
        this.list = l;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

}
