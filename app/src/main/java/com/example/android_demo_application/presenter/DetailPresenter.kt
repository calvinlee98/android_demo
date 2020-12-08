package com.example.android_demo_application.presenter

import com.example.android_demo_application.activities.DetailActivity
import com.example.android_demo_application.activities.DetailView
import com.example.android_demo_application.entities.ShouyeItem
import com.example.android_demo_application.utils.HttpUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailPresenter {
    var mView: DetailView? = null
    fun attachView(view: DetailView) {
        mView= view
    }

    fun detachView() {
        mView = null
    }

    fun likeArticle(article_id:String){
        val observable = Observable.create<Boolean> {
            val flag = HttpUtils.likeArticle(article_id)
            it.onNext(flag)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        val observer: Observer<Boolean> = object : Observer<Boolean> {
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {}
            override fun onComplete() {}
            override fun onNext(flag:Boolean) {
                mView?.likeArticle()
            }
        }
        observable.subscribe(observer)
    }
}