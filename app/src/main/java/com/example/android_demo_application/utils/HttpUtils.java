package com.example.android_demo_application.utils;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
    public static int GET = 1;
    public static int POST = 2;
    public static String LOGIN_URL = "https://www.wanandroid.com/user/login";
    public static String login(String username,String password){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("username","lifangzheng").add("password","123456").build();
        Request request = new Request.Builder().url(LOGIN_URL).post(requestBody).build();
        Response response = null;
        String responseData = "";
        try {
            response = client.newCall(request).execute();
            responseData = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseData;
    }
}
