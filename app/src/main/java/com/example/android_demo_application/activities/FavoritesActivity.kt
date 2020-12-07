package com.example.android_demo_application.activities

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.android_demo_application.MyApplication
import com.example.android_demo_application.R
import com.example.android_demo_application.fragment_adapters.FavoriteArticlesAdapter
import com.example.android_demo_application.utils.HttpUtils
import com.example.android_demo_application.utities.ShouyeItem
import java.lang.ref.WeakReference

class FavoritesActivity : AppCompatActivity(),FavoritesView {
    var presenter : FavoritesPresenter? = null
    private var curr_page = 0
    var recyclerView: RecyclerView? = null
    var adapter: FavoriteArticlesAdapter? = null
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var mLastVisibleItemPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorites)
        init()
        curr_page = 0
        presenter?.getFirstUIData(curr_page)
    }

    private fun init() {
        presenter = FavoritesPresenter()
        presenter!!.attachView(this)
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout)
        swipeRefreshLayout?.setOnRefreshListener {
            //上滑刷新
            curr_page = 0
           presenter?.getFirstUIData(curr_page)
            //zhu线程上
            swipeRefreshLayout?.isRefreshing = false
        }
        recyclerView = findViewById(R.id.rv)
        //加载更多的listener
        recyclerView?.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val layoutManager = recyclerView.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    mLastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                }
                if (adapter != null) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItemPosition + 1 == adapter!!.itemCount) {
                       presenter?.getMoreUIData(curr_page)
                    }
                }
            }
        })
        adapter = FavoriteArticlesAdapter()
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(this)
    }

//    internal class MyHandler(activity: FavoritesActivity?) : Handler() {
//        var weakReference: WeakReference<FavoritesActivity?> = WeakReference(activity)
//        override fun handleMessage(msg: Message) {
//            super.handleMessage(msg)
//            if (weakReference.get() != null) {
//                weakReference.get()!!.adapter!!.list = msg.obj as ArrayList<ShouyeItem?>
//                weakReference.get()!!.adapter!!.notifyDataSetChanged()
//            }
//        }
//
//    }

//    var handler: Handler = MyHandler(this)
//    var addMoreHandler: Handler = AddMoreHandler(this)

//    internal class AddMoreHandler(activity: FavoritesActivity?) : Handler() {
//        var mActivity: WeakReference<FavoritesActivity?> = WeakReference(activity)
//        override fun handleMessage(msg: Message) {
//            super.handleMessage(msg)
//            if (mActivity.get() != null) {
//                val list = msg.obj as List<ShouyeItem>
//                for (item in list) mActivity.get()!!.adapter!!.list.add(item)
//                mActivity.get()!!.adapter!!.notifyDataSetChanged()
//            }
//        }
//
//    }

    override fun initRecyclerView(list: MutableList<ShouyeItem>) {
        adapter?.list = list
        adapter?.notifyDataSetChanged()
        curr_page++
    }

    override fun addMoreViewsToRecyclerView(list: MutableList<ShouyeItem>) {
        if(list.size == 0){
            return
        }
        for(item in list)
            adapter?.list?.add(item)
        adapter?.notifyDataSetChanged()
        curr_page++
    }
}