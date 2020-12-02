package com.example.android_demo_application.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.android_demo_application.R;
import com.example.android_demo_application.fragments.LoginFragment;
import com.example.android_demo_application.fragments.RegisterFragment;

public class LogActivity extends AppCompatActivity {
    public int STATE = 0;
    private static int LOGIN = 0;
    private static int REGISTER = 1;
    Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_log);
        init();
    }
    void init(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_container,new LoginFragment()).commit();


        button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager1.beginTransaction();
            if(STATE==LOGIN){
                //转移到 注册页面
                STATE = REGISTER;
                transaction.replace(R.id.fragment_container,new RegisterFragment()).commit();
                button.setText(R.string.qudenglu);
            }
            else{
                STATE = LOGIN;
                transaction.replace(R.id.fragment_container,new LoginFragment()).commit();
                button.setText(R.string.quzhuce);
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if(imm.isActive()&&getCurrentFocus()!=null){
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken()!=null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return true;
    }
}
