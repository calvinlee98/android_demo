package com.example.android_demo_application.utils;

import android.content.SharedPreferences;

import com.example.android_demo_application.MyApplication;

public class SharedPreferenceUtils {
    private static String USER_NAME = "username";
    private static String PASSWORD = "password";

    public static SharedPreferences sharedPreferences  = MyApplication.getContext().getSharedPreferences("user_info",0);

    public static SharedPreferences.Editor editor = sharedPreferences.edit();
    public static String getSavedUserName(){
        return sharedPreferences.getString(USER_NAME,"");
    }
    public static String getSavedPassword(){
        return sharedPreferences.getString(PASSWORD,"");
    }
    public static void saveUserInfo(String username,String password){
        editor.putString(USER_NAME,username);
        editor.putString(PASSWORD,password);
        editor.apply();
    }

    public static void empty(){
        editor.clear();
        editor.apply();
    }
}
