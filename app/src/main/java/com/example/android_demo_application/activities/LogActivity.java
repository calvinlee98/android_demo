package com.example.android_demo_application.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }
}
