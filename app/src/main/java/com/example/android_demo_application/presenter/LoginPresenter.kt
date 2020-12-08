package com.example.android_demo_application.presenter

import com.example.android_demo_application.fragments.LoginView
import com.example.android_demo_application.utils.HttpUtils
import io.reactivex.Observable

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class LoginPresenter {
    private var mView: LoginView? = null
    fun attachView(view: LoginView){
        mView = view
    }
    fun detachView(){
        mView = null
    }
    fun login(userName:String
              , userPassword: String){
        val observable = Observable.create<String> {
            val s = HttpUtils.login(userName,userPassword)
            it.onNext(s)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        val observer: Observer<String> = object : Observer<String> {
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: String) {
                if(t=="")
                    mView?.onSuccess()
                else
                    mView?.onFail(t)
            }
        }
        observable.subscribe(observer)

    }
}