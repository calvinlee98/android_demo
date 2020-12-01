package com.example.android_demo_application.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {


    public static String LOGIN_URL = "https://www.wanandroid.com/user/login";
    public static String REGISTER_URL = "https://www.wanandroid.com/user/register";
    public static String LOGOUT_URL = "https://www.wanandroid.com/user/logout/json";

  public static String login(String username,String passoword){
      RequestBody requestBody = new FormBody.Builder().add("username",username).add("password",passoword).build();
      return postGetErrorMsg(LOGIN_URL,requestBody);
  }
  public static String register(String username,String password,String repassword){
      RequestBody requestBody = new FormBody.Builder().add("username",username).add("password",password).add("repassword",repassword).build();
      return postGetErrorMsg(REGISTER_URL,requestBody);
  }
   public static String postGetErrorMsg(String url,RequestBody requestBody){
       OkHttpClient okHttpClient = new OkHttpClient();

       Request request = new Request.Builder().url(url).post(requestBody).build();

       Response response = null;
       String responseData = null;

       try {
           response = okHttpClient.newCall(request).execute();
           responseData = response.body().string();
       } catch (IOException e) {
           return "error！ retry";
       }
       JSONObject jsonObject = null;
       try {
           jsonObject = new JSONObject(responseData);
           return jsonObject.getString("errorMsg");
       } catch (JSONException e) {
           return "格式错误！";
       }


   }
}
