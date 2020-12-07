package com.example.android_demo_application.activities

import com.example.android_demo_application.utils.HttpUtils
import com.example.android_demo_application.utities.ShouyeItem
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FavoritesPresenter {
    var view: FavoritesView? = null
    fun attachView(view: FavoritesView?) {
        this.view = view
    }

   fun getFirstUIData(cur_page: Int) {
        val observable = Observable.create<List<ShouyeItem>> {
            val list = HttpUtils.getFavorites(cur_page)
            it.onNext(list)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        val observer: Observer<List<ShouyeItem>> = object : Observer<List<ShouyeItem>> {
            override fun onSubscribe(d: Disposable) {}
            override fun onError(e: Throwable) {}
            override fun onComplete() {}
            override fun onNext(t: List<ShouyeItem>) {
                view?.initRecyclerView(t)
            }
        }
        observable.subscribe(observer)
    }

    fun getMoreUIData(cur_page: Int){
        val observable = Observable.create<List<ShouyeItem>> {
            var list = HttpUtils.getFavorites(cur_page)
            it.onNext(list)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        val observer: Observer<List<ShouyeItem>> = object  : Observer<List<ShouyeItem>>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: List<ShouyeItem>) {
                view?.addMoreViewsToRecyclerView(t)
            }

            override fun onError(e: Throwable) {
            }

        }
        observable.subscribe(observer)

    }
}


