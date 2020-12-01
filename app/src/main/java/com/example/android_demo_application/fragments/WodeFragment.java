package com.example.android_demo_application.fragments;

/*
* author by @李方正
* 我的 界面的fragment
*
* */
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.android_demo_application.MyApplication;
import com.example.android_demo_application.R;
import com.example.android_demo_application.activities.LogActivity;
import com.example.android_demo_application.views.MyButton;


public class WodeFragment extends Fragment implements View.OnClickListener {
    View view;
    Button button;
    //MyButton extends FrameLayout  是一个FrameLayout
    MyButton wodejifen;
    MyButton wodefenxiang;
    MyButton wodeshoucang;
    MyButton shaohouyuedu;
    MyButton yuedulishi;
    MyButton kaiyuanxiangmu;
    MyButton guanyuzuozhe;
    MyButton xitongshezhi;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wode,container,false);
        initView();
        return view;
    }

    void initView(){
        button = view.findViewById(R.id.qudenglu);
        wodejifen = view.findViewById(R.id.wodejifen);
        wodefenxiang = view.findViewById(R.id.wodefenxiang);
        wodeshoucang = view.findViewById(R.id.wodeshoucang);
        shaohouyuedu = view.findViewById(R.id.shaohouyuedu);
        yuedulishi = view.findViewById(R.id.yuedulishi);
        kaiyuanxiangmu = view.findViewById(R.id.kaiyuanxiangmu);
        guanyuzuozhe = view.findViewById(R.id.guanyuzuozhe);
        xitongshezhi = view.findViewById(R.id.xitongshezhi);





        //设置每个复合button的图片 文字资源
        wodejifen.set(R.drawable.wodejifen,R.string.wodejifen);
        wodefenxiang.set(R.drawable.wodefenxiang,R.string.wodefenxiang);
        wodeshoucang.set(R.drawable.wodeshoucang,R.string.wodeshoucang);
        shaohouyuedu.set(R.drawable.shaohouyuedu,R.string.shaohouyuedu);
        yuedulishi.set(R.drawable.yuedulishi,R.string.yuedulishi);
        kaiyuanxiangmu.set(R.drawable.kaiyuanxiangmu,R.string.kaiyuanxiangmu);
        guanyuzuozhe.set(R.drawable.guanyuzuozhe,R.string.guanyuzuozhe);
        xitongshezhi.set(R.drawable.xitongshezhi,R.string.xitongshezhi);








     button.setOnClickListener(this::onClick);
     wodejifen.setOnClickListener(this::onClick);
     wodefenxiang.setOnClickListener(this::onClick);
     wodeshoucang.setOnClickListener(this::onClick);
     shaohouyuedu.setOnClickListener(this::onClick);
     yuedulishi.setOnClickListener(this::onClick);
     kaiyuanxiangmu.setOnClickListener(this::onClick);
     guanyuzuozhe.setOnClickListener(this::onClick);
     xitongshezhi.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.qudenglu:
                Intent intent = new Intent(getActivity(), LogActivity.class);
                startActivity(intent);
                break;
            case R.id.wodejifen:
                Toast.makeText(MyApplication.getContext(),"我的积分",Toast.LENGTH_SHORT).show();
                break;
            case R.id.wodefenxiang:
                Toast.makeText(MyApplication.getContext(),"我的分享",Toast.LENGTH_SHORT).show();
                break;
                case R.id.wodeshoucang:
                Toast.makeText(MyApplication.getContext(),"我的收藏",Toast.LENGTH_SHORT).show();
                break;
            case R.id.shaohouyuedu:
                Toast.makeText(MyApplication.getContext(),"稍后阅读",Toast.LENGTH_SHORT).show();
                break;
            case R.id.yuedulishi:
                Toast.makeText(MyApplication.getContext(),"阅读历史",Toast.LENGTH_SHORT).show();
                break;
            case R.id.kaiyuanxiangmu:
                Toast.makeText(MyApplication.getContext(),"开源项目",Toast.LENGTH_SHORT).show();
                break;
            case R.id.guanyuzuozhe:
                Toast.makeText(MyApplication.getContext(),"关于作者",Toast.LENGTH_SHORT).show();
                break;
            case R.id.xitongshezhi:
                Toast.makeText(MyApplication.getContext(),"系统设置",Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
