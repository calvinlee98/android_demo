package com.example.android_demo_application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.litepal.LitePalApplication;

public class MyApplication extends Application {
    private static Context context;
    private static String TAG = ".MyApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        //登录时启动
        Log.v(TAG,"应用启动");
        context = getApplicationContext();
        LitePalApplication.initialize(context);
    }
    public Context getContext(){
        return context;//全局获取context   application级context
    }
}
