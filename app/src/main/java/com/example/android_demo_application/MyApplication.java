package com.example.android_demo_application;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android_demo_application.tasks.LoginAsyncTask;
import com.example.android_demo_application.utils.HttpUtils;
import com.example.android_demo_application.utils.SharedPreferenceUtils;

import org.litepal.LitePalApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyApplication extends Application {
    private static boolean isLoggedIn = false;
    private static String userName = "";

    public static void setIsLoggedIn(boolean b){
        isLoggedIn = b;
    }

    public static boolean isIsLoggedIn(){
        return isLoggedIn;
    }

    public static void setUserName(String name){
        userName = name;
    }

    public static String getUserName(){
        return userName;
    }
    private static ExecutorService pools = Executors.newFixedThreadPool(10);//初始化一个线程池
    private static Context context;
    private static String TAG = ".MyApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        //登录时启动
        Log.v(TAG,"应用启动");
        context = getApplicationContext();
        LitePalApplication.initialize(context);



        //尝试登录
       new LoginAsyncTask().execute();
    }
    public static Context getContext(){
        return context;//全局获取context   application级context
    }
    public static ExecutorService getPools(){
        return pools;
    }
}
