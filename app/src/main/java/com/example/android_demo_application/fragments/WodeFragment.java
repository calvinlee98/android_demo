package com.example.android_demo_application.fragments;

/*
* author by @李方正
* 我的 界面的fragment
*
* */
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
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

import com.example.android_demo_application.activities.FavoritesActivity;
import com.example.android_demo_application.activities.LogActivity;
import com.example.android_demo_application.utils.HttpUtils;
import com.example.android_demo_application.utils.SharedPreferenceUtils;
import com.example.android_demo_application.views.MyButton;


public class WodeFragment extends Fragment implements View.OnClickListener {
    private static String TAG = ".WodeFragment";
     Button button_logout;
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
        button_logout = view.findViewById(R.id.logout);
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
     button_logout.setOnClickListener(this::onClick);
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            if (MyApplication.isIsLoggedIn()) {
                button.setText(MyApplication.getUserName());
                button_logout.setVisibility(View.VISIBLE);
            } else {
                button.setText(R.string.qudenglu);
                button_logout.setVisibility(View.GONE);
            }
        }

    }



    @Override
    public void onResume() {
        super.onResume();
        if (MyApplication.isIsLoggedIn()) {
            button.setText(MyApplication.getUserName());
            button_logout.setVisibility(View.VISIBLE);
        } else {
            button.setText(R.string.qudenglu);
            button_logout.setVisibility(View.GONE);
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String s = (String) msg.obj;
            if(s.equals("")){
                //没有错误
                //登出
                MyApplication.setIsLoggedIn(false);
                MyApplication.setUserName("");
                //清空缓存
                SharedPreferenceUtils.empty();

                button_logout.setVisibility(View.GONE);
                button.setText(R.string.qudenglu);
                view.requestLayout();

            }
            else {
                Toast.makeText(MyApplication.getContext(),s,Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.qudenglu:
                Intent intent = new Intent(getActivity(), LogActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                //登出逻辑
                //发送http请求
                //更新UI    登出应该消失
                //删除本地 SharedPreference
                 MyApplication.getPools().execute(new Runnable() {
                     @Override
                     public void run() {
                         String s = HttpUtils.logout();
                         Message message = Message.obtain();
                         message.setTarget(handler);
                         message.obj = s;
                         handler.sendMessage(message);
                     }
                 });
                 break;
            case R.id.wodejifen:
                Toast.makeText(MyApplication.getContext(),"我的积分",Toast.LENGTH_SHORT).show();
                break;
            case R.id.wodefenxiang:
                Toast.makeText(MyApplication.getContext(),"我的分享",Toast.LENGTH_SHORT).show();
                break;
                case R.id.wodeshoucang:
                    Intent intent1;
                if(MyApplication.isIsLoggedIn()){
                       intent1 = new Intent(getActivity(), FavoritesActivity.class);
                       startActivity(intent1);
                }
                else{
                    intent1 = new Intent(getActivity(),LogActivity.class);
                    startActivity(intent1);
                }
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
