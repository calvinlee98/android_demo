package com.example.android_demo_application.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.android_demo_application.R;
import com.example.android_demo_application.fragment_adapters.MainActivityAdapter;
import com.example.android_demo_application.fragments.WodeFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager pager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initTab();
    }
    private void initView(){
        //  view的初始化
        tabLayout = findViewById(R.id.tabs);
        pager = findViewById(R.id.pager);

        //创建一个fragments list
        List<Fragment>list = new ArrayList<>();
        list.add(new WodeFragment());
        list.add(new WodeFragment());
        list.add(new WodeFragment());
        list.add(new WodeFragment());

        //主页  viewpager 的 adapter
        MainActivityAdapter adapter = new MainActivityAdapter(getSupportFragmentManager(),list);

        //pager
        pager.setAdapter(adapter);

        tabLayout.setupWithViewPager(pager);

    }
    private void initTab(){
        TabLayout.Tab tab;
        tab = tabLayout.getTabAt(0);
        tab.setText(R.string.shouye);
        tab.setIcon(R.drawable.shouye);
        tab = tabLayout.getTabAt(1);
        tab.setText(R.string.wenda);
        tab.setIcon(R.drawable.wenda);
        tab = tabLayout.getTabAt(2);
        tab.setText(R.string.tixi);
        tab.setIcon(R.drawable.tixi);
        tab = tabLayout.getTabAt(3);
        tab.setText(R.string.wode);
        tab.setIcon(R.drawable.wode);
    }
}
