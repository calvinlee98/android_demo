package com.example.android_demo_application.tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.android_demo_application.MyApplication;
import com.example.android_demo_application.utils.HttpUtils;
import com.example.android_demo_application.utils.SharedPreferenceUtils;

public class LoginAsyncTask extends AsyncTask<Void,Integer,String> {
    @Override
    protected String doInBackground(Void... voids) {
        String username = SharedPreferenceUtils.getSavedUserName();
        String password = SharedPreferenceUtils.getSavedPassword();
        return HttpUtils.login(username,password);
    }

    @Override
    protected void onPostExecute(String s) {
        if(s.equals("")){
            //登录成功了
            MyApplication.setIsLoggedIn(true);
            MyApplication.setUserName(SharedPreferenceUtils.getSavedUserName());
        }
        else{
            MyApplication.setIsLoggedIn(false);
            MyApplication.setUserName("");
        }

    }
}
