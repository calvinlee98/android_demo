package com.example.android_demo_application.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.android_demo_application.R
import com.example.android_demo_application.fragment_adapters.FavoriteArticlesAdapter
import com.example.android_demo_application.entities.ShouyeItem

class FavoritesActivity : AppCompatActivity(),FavoritesView {

    lateinit var presenter : FavoritesPresenter
    private var curr_page = 0
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: FavoriteArticlesAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var mLastVisibleItemPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorites)
        init()
        curr_page = 0
        presenter.getFirstUIData(curr_page)
    }

    private fun init() {
        presenter = FavoritesPresenter()
        presenter.attachView(this)
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout)
        swipeRefreshLayout.setOnRefreshListener {
            //上滑刷新
            curr_page = 0
            presenter.getFirstUIData(curr_page)
            //zhu线程上
            swipeRefreshLayout.isRefreshing = false
        }
        recyclerView = findViewById(R.id.rv)
        //加载更多的listener
        recyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val layoutManager = recyclerView.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    mLastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItemPosition + 1 == adapter.itemCount) {
                    presenter.getMoreUIData(curr_page)
                }
            }
        })
        adapter = FavoriteArticlesAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun initRecyclerView(list: MutableList<ShouyeItem>) {
        adapter.list = list
        adapter.notifyDataSetChanged()
        curr_page++
    }

    override fun addMoreViewsToRecyclerView(list: MutableList<ShouyeItem>) {
        if(list.size == 0){
            return
        }
        for(item in list)
            adapter.list.add(item)
        adapter.notifyDataSetChanged()
        curr_page++
    }
}