package com.example.android_demo_application

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.android_demo_application.utils.HttpUtils
import com.example.android_demo_application.utils.SharedPreferenceUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.litepal.LitePalApplication
import java.util.concurrent.Executors

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //登录时启动
        Log.v(TAG, "应用启动")
        context = applicationContext
        LitePalApplication.initialize(context)


        //尝试登录
       // LoginAsyncTask().execute()


        val observable =
            Observable.create<String> { emitter ->
                //  Schedulers.io()线程上
                Log.v("Thread",Thread.currentThread().toString())
                val string = HttpUtils.login(SharedPreferenceUtils.savedUserName,SharedPreferenceUtils.savedPassword)
                emitter.onNext(string)
                emitter.onComplete()
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        val observer: Observer<String> =
            object : Observer<String> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(s: String) {
                    Log.v("Thread",Thread.currentThread().toString())
                    if (s == "") {
                        setIsLoggedIn(true)
                      userName = SharedPreferenceUtils.savedUserName.toString()
                    } else {
                        setIsLoggedIn(false)
                        userName = ""
                    }
                }

                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            }
        observable.subscribe(observer)

    }

    companion object {
        var isLoggedIn = false
        var userName = ""
        fun setIsLoggedIn(b: Boolean) {
            isLoggedIn = b
        }

        val pools =
            Executors.newFixedThreadPool(10) //初始化一个线程池

        //全局获取context   application级context
        var context: Context? = null
            private set
        private const val TAG = ".MyApplication"

    }
}