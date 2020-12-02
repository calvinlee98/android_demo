package com.example.android_demo_application.fragments;

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

public class RegisterFragment extends Fragment {
    View view;
    TextInputEditText input_username;
    TextInputEditText input_email;
    TextInputEditText input_password;
    TextInputEditText input_repassword;
    Button button;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_register,container,false);
         init();
        return view;
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if(result.equals("")){
                //成功注册了
                MyApplication.setUserName(input_username.getText().toString());
                MyApplication.setIsLoggedIn(true);
                SharedPreferenceUtils.saveUserInfo(input_username.getText().toString(),
                        input_password.getText().toString());
                getActivity().finish();
            }
            else{
                Toast.makeText(MyApplication.getContext(),result,Toast.LENGTH_SHORT).show();
            }
        }
    };
    private void init(){
        input_username = view.findViewById(R.id.text_input_username);
        input_email = view.findViewById(R.id.text_input_email);
        input_password = view.findViewById(R.id.text_input_password);
        input_repassword = view.findViewById(R.id.text_input_repassword);
        button = view.findViewById(R.id.button);
        button.setOnClickListener(v -> MyApplication.getPools().execute(() -> {
            String result = HttpUtils.register(input_username.getText().toString(),
                    input_password.getText().toString(),
                    input_repassword.getText().toString());
            Message message = Message.obtain();
            message.obj = result;
            message.setTarget(handler);
            handler.sendMessage(message);
        }));
    }
}
