package com.example.android_demo_application.fragments;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android_demo_application.MyApplication;
import com.example.android_demo_application.R;
import com.example.android_demo_application.utils.HttpUtils;
import com.example.android_demo_application.utils.SharedPreferenceUtils;
import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment {
    TextInputEditText yonghuming;
    TextInputEditText mima;
    Button button;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        yonghuming = view.findViewById(R.id.text_intput_username);
        mima = view.findViewById(R.id.text_input_password);
        button = view.findViewById(R.id.denglu);
        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                //主线程上

                String s = (String) msg.obj;
                if(s.equals("")){
                    //保存 登录逻辑
                    SharedPreferenceUtils.saveUserInfo(yonghuming.getText().toString(),mima.getText().toString());
                    MyApplication.setIsLoggedIn(true);
                    MyApplication.setUserName(yonghuming.getText().toString());
                    getActivity().finish();
                    return;
                }
                MyApplication.setIsLoggedIn(false);
                MyApplication.setUserName("");
                    SharedPreferenceUtils.empty();
                    Toast.makeText(MyApplication.getContext(),s,Toast.LENGTH_SHORT).show();
            }
        };
        button.setOnClickListener(v ->
                MyApplication.getPools().execute(() -> {
                    String string = HttpUtils.login(yonghuming.getText().toString(),mima.getText().toString());
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    message.obj = string;
                    handler.sendMessage(message);
                }
                )
        );
        return view;
    }
}
