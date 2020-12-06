package com.example.android_demo_application.utils

import com.example.android_demo_application.MyApplication

object SharedPreferenceUtils {

    private const val USER_NAME = "username"
    private const val PASSWORD = "password"
    var sharedPreferences = MyApplication.context?.getSharedPreferences("user_info", 0)
    var editor = sharedPreferences?.edit()
     val savedUserName: String?
        get() = sharedPreferences?.getString(USER_NAME, "")

   val savedPassword: String?
        get() = sharedPreferences?.getString(PASSWORD, "")

    @JvmStatic
    fun saveUserInfo(username: String?, password: String?) {
        editor!!.putString(USER_NAME, username)
        editor!!.putString(PASSWORD, password)
        editor!!.apply()
    }

    fun empty() {
        editor!!.clear()
        editor!!.apply()
    }
}