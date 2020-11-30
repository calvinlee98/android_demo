package com.example.android_demo_application.fragments;

/*
* author by @李方正
* 我的 界面的fragment
*
* */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.android_demo_application.R;


public class WodeFragment extends Fragment {
    View view;
    TextView textView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wode,container,false);
        initView();
        return view;
    }
    void initView(){
        textView = view.findViewById(R.id.qudenglu);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动一个新的activity  去登陆或者去注册
            }
        });
    }
}
