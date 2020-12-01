package com.example.android_demo_application.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.example.android_demo_application.IInternetInterface;
import com.example.android_demo_application.utils.HttpUtils;

public class InternetService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return  new IInternetInterfaceImpl();
    }
}
 class  IInternetInterfaceImpl extends IInternetInterface.Stub{

     @Override
     public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

     }

     @Override
     public String login(String username, String password) throws RemoteException {
         return HttpUtils.login(username,password);
     }

     @Override
     public String register(String username, String password, String repassword) throws RemoteException {
         return null;
     }

     @Override
     public String logout() throws RemoteException {
         return null;
     }
 }