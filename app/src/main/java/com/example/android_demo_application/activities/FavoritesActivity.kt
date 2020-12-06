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

class FavoritesActivity : AppCompatActivity() {
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
        MyApplication.pools.execute {
            val list = HttpUtils.getFavorites(curr_page++)
            val message = Message.obtain()
            message.obj = list
            message.target = handler
            handler.sendMessage(message)
        }
    }

    private fun init() {
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout)
        swipeRefreshLayout?.setOnRefreshListener(OnRefreshListener {
            MyApplication.pools.execute {
                curr_page = 0
                val list = HttpUtils.getFavorites(curr_page++)
                val message = Message.obtain()
                message.obj = list
                message.target = handler
                handler.sendMessage(message)
            }
            //zhu线程上
            swipeRefreshLayout?.setRefreshing(false)
        })
        recyclerView = findViewById(R.id.rv)
        recyclerView?.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val layoutManager = recyclerView.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    mLastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                }
                if (adapter != null) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItemPosition + 1 == adapter!!.itemCount) {
                        Log.v(".RecyclerView.Adapter", "尝试加载更多")
                        MyApplication.pools.execute(Runnable {
                            Log.v(".RecyclerView", "当前尝试加载$curr_page")
                            val list = HttpUtils.getFavorites(curr_page)
                            if (list.size == 0) {
                                return@Runnable
                            }
                            curr_page++
                            val message = Message.obtain()
                            message.target = addMoreHandler
                            message.obj = list
                            addMoreHandler.sendMessage(message)
                        })
                    }
                }
            }
        })
        adapter = FavoriteArticlesAdapter(handler)
        recyclerView?.setAdapter(adapter)
        recyclerView?.setLayoutManager(LinearLayoutManager(this))
    }

    internal class MyHandler(activity: FavoritesActivity?) : Handler() {
        var weakReference: WeakReference<FavoritesActivity?>
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (weakReference.get() != null) {
                weakReference.get()!!.adapter!!.list = msg.obj as ArrayList<ShouyeItem?>
                weakReference.get()!!.adapter!!.notifyDataSetChanged()
            }
        }

        init {
            weakReference = WeakReference(activity)
        }
    }

    var handler: Handler = MyHandler(this)
    var addMoreHandler: Handler = AddMoreHandler(this)

    internal class AddMoreHandler(activity: FavoritesActivity?) : Handler() {
        var mActivity: WeakReference<FavoritesActivity?>
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (mActivity.get() != null) {
                val list = msg.obj as List<ShouyeItem>
                for (item in list) mActivity.get()!!.adapter!!.list.add(item)
                mActivity.get()!!.adapter!!.notifyDataSetChanged()
            }
        }

        init {
            mActivity = WeakReference(activity)
        }
    }
}