package com.example.android_demo_application.utils;

import android.util.Log;

import com.example.android_demo_application.activities.LogActivity;
import com.example.android_demo_application.utities.ShouyeItem;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {


    public static String LOGIN_URL = "https://www.wanandroid.com/user/login";
    public static String REGISTER_URL = "https://www.wanandroid.com/user/register";
    public static String ARTICLES_LIST = "https://www.wanandroid.com/article/list/";
    public static String LOGOUT_URL = "https://www.wanandroid.com/user/logout/json";


    public static String FAVORITES_ARTICLES = "https://www.wanandroid.com/lg/collect/list/";//需要拼接  get方法
    public static String LIKE_ARTICLE = "https://www.wanandroid.com/lg/collect/";// 需要拼接  post方法
    public static String CANCEL_LIKE = "https://www.wanandroid.com/lg/uncollect_originId/";//需要拼接  post方法




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

   public static List<ShouyeItem> getLists(int page)  {
      String url = ARTICLES_LIST+page+"/json";

      OkHttpClient client = new OkHttpClient();

      Request request = new Request.Builder().url(url).build();

      Response response = null;
      String responseData = null;

       try {
           response = client.newCall(request).execute();
           responseData = response.body().string();

           JSONObject jsonObject = new JSONObject(responseData);
           JSONObject jsonObject1 = jsonObject.getJSONObject("data");
           JSONArray jsonArray = jsonObject1.getJSONArray("datas");
           List<ShouyeItem>list = new ArrayList<>();
           for(int i=0;i<jsonArray.length();++i){
               JSONObject object = jsonArray.getJSONObject(i);
               list.add(new ShouyeItem(
                       object.getString("id"),
                       object.getString("author"),
                       object.getString("publishTime"),
                       object.getString("title"),
                       "",
                       object.getString("superChapterName"),
                       object.getString("link")));
           }
          return list;
       } catch (Exception e) {
           return new ArrayList<>();
       }

   }

   public static String logout(){
      OkHttpClient client = new OkHttpClient();
      Request request = new Request.Builder().url(LOGOUT_URL).build();

      Response response = null;
      String responseData = null;

      try{
      response = client.newCall(request).execute();
      responseData = response.body().string();
      JSONObject jsonObject = new JSONObject(responseData);
      return jsonObject.getString("errorMsg");
      }
      catch (Exception e){
          return "登出错误！";
      }
   }

   public static List<ShouyeItem> getFavorites(int page){
      OkHttpClient client = new OkHttpClient();

      String cookieInfo = "loginUserName="+SharedPreferenceUtils.getSavedUserName()+";loginUserPassword="+SharedPreferenceUtils.getSavedPassword();

      Request request = new Request.Builder().url(FAVORITES_ARTICLES+page+"/json").header("Cookie",cookieInfo).build();

      Response response = null;
      String responseData = null;

      try{
          response = client.newCall(request).execute();
          responseData = response.body().string();
          Log.d("TAG",responseData);

          JSONObject jsonObject = new JSONObject(responseData);
          JSONObject jsonObject1 = jsonObject.getJSONObject("data");
          JSONArray jsonArray = jsonObject1.getJSONArray("datas");
          List<ShouyeItem>list = new ArrayList<>();
          for(int i=0;i<jsonArray.length();++i){
              JSONObject object = jsonArray.getJSONObject(i);
              list.add(new ShouyeItem(
                      object.getString("originId"),
                      object.getString("author"),
                      object.getString("publishTime"),
                      object.getString("title"),
                      "",
                      object.getString("chapterName"),
                      object.getString("link")));
      }
          return list;
      }
      catch (Exception e){
          return new ArrayList<>();

   }
}
    public static String cancelLike(String page_id){
       String url = CANCEL_LIKE+page_id+"/json";
       String cookieInfo = "loginUserName="+SharedPreferenceUtils.getSavedUserName()+";loginUserPassword="+SharedPreferenceUtils.getSavedPassword();
       OkHttpClient client  = new OkHttpClient();
       RequestBody requestBody = new FormBody.Builder().build();
       Request request = new Request.Builder().url(url).post(requestBody).header("Cookie",cookieInfo).build();

       Response response = null;
       String responseData = null;

       try {
       response = client.newCall(request).execute();
       responseData = response.body().string();
       Log.d("TAG",responseData);
       return new JSONObject(responseData).getString("errorMsg");
       }catch (Exception e){
           return "取消收藏错误！";
       }
    }
}
