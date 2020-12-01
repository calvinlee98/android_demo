package com.example.android_demo_application.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.example.android_demo_application.ILogAndRegisterInterface;
import com.example.android_demo_application.utils.HttpUtils;

public class ILogAndRegisterService extends Service {
    IBinder binder = new ILogAndRegisterInterfaceImpl();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public static class ILogAndRegisterInterfaceImpl extends ILogAndRegisterInterface.Stub{

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public String login(String username, String password) throws RemoteException {
            return HttpUtils.login(username,password);//返回错误信息  若为空 登录成功   本地持久化 username password
        }

        @Override
        public String register(String username, String password, String repassword) throws RemoteException {
            return HttpUtils.register(username,password,repassword);
        }

        @Override
        public void logout() throws RemoteException {

        }
    }
}
