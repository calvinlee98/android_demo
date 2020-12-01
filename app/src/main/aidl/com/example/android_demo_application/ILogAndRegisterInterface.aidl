// ILogAndRegisterInterface.aidl
package com.example.android_demo_application;

// Declare any non-default types here with import statements

interface ILogAndRegisterInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
    String login(String username,String password);//返回errorMsg
    String register(String username,String password,String repassword);
    void logout();
}