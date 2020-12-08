package com.example.android_demo_application.fragments

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_demo_application.MyApplication
import com.example.android_demo_application.R
import com.example.android_demo_application.fragment_adapters.ShouyeAdapter
import com.example.android_demo_application.utils.HttpUtils
import com.example.android_demo_application.entities.ShouyeItem
import kotlinx.android.synthetic.main.shouye.view.*
import kotlinx.android.synthetic.main.title_bar.view.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class ShouyeFragment : Fragment() {
    companion object {
        const val favoriteIntentFilterAction = "com.example.android_demo.favorite"
    }
    // message type
    private val refreshSuccess = 1
    private val refreshFail = 2
    private val moreSuccess = 3
    private val moreFail = 4

    // the page number for more button
    private var nextPage = 1

    // id
    @Volatile private var refreshId = 0
    @Volatile private var moreId = 0

    // view
    private lateinit var fragmentView: View
    private val progressDialog by lazy {
        ProgressDialog(activity)
    }
    private val recyclerView by lazy {
        fragmentView.shouyeRecyclerView
    }

    // variables
    private val _itemList = ArrayList<ShouyeItem>()
    private val _bannerList = ArrayList<ShouyeBannerFragment>()
    private val _favoriteSet = HashSet<String>()

    // inner object for message
    inner class MessageObj(val id: Int, val obj: Any?)
    inner class RefreshObj(val itemList: List<ShouyeItem>, val bannerList: List<ShouyeBannerFragment>, val favoriteSet: Set<String>)

    // broadcast
    private val favoriteChangeReceiver: FavoriteChangeReceiver by lazy {
        FavoriteChangeReceiver()
    }
    inner class FavoriteChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // 根据消息内容更改favoriteSet，并且刷新recyclerView
            val flag = intent.getStringExtra("flag")
            val articleId = intent.getStringExtra("articleId")
            if (articleId != null) {
                if (flag == "add") {
                    _favoriteSet.add(articleId)
                } else if (flag == "remove") {
                    _favoriteSet.remove(articleId)
                }
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                refreshSuccess -> {
                    val messageObj = msg.obj as MessageObj
                    if (messageObj.id == refreshId) {
                        refreshId = if (refreshId == 100) 0 else refreshId + 1

                        if (progressDialog.isShowing) {
                            progressDialog.dismiss()
                        }

                        // 清空并重设数据集，初始化nextPage变量
                        _itemList.clear()
                        _bannerList.clear()
                        _favoriteSet.clear()
                        nextPage = 1

                        val refreshObj = messageObj.obj as RefreshObj
                        _itemList.addAll(refreshObj.itemList)
                        _bannerList.addAll(refreshObj.bannerList)
                        _favoriteSet.addAll(refreshObj.favoriteSet)

                        // 清空并重设recycler view
                        if (recyclerView.childCount > 0) {
                            recyclerView.removeAllViews()
                        }
                        notifyRecyclerView(0)
                    }
                }
                refreshFail -> {
                    val messageObj = msg.obj as MessageObj
                    if (messageObj.id == refreshId) {
                        refreshId = if (refreshId == 100) 0 else refreshId + 1

                        if (progressDialog.isShowing) {
                            progressDialog.dismiss()
                        }

                        _itemList.clear()
                        _bannerList.clear()
                        _favoriteSet.clear()
                        nextPage = 1

                        if (recyclerView.childCount > 0) {
                            recyclerView.removeAllViews()
                        }
                        recyclerView.adapter?.notifyDataSetChanged()

                        Toast.makeText(activity, "refresh fail", Toast.LENGTH_SHORT).show()
                    }
                }
                moreSuccess -> {
                    val messageObj = msg.obj as MessageObj
                    if (messageObj.id == moreId) {
                        moreId = if (moreId == 100) 0 else moreId + 1

                        if (progressDialog.isShowing) {
                            progressDialog.dismiss()
                        }

                        nextPage += 1

                        val size = _itemList.size + 1
                        _itemList.addAll(messageObj.obj as List<ShouyeItem>)
                        notifyRecyclerView(size)
                    }
                }
                moreFail -> {
                    val messageObj = msg.obj as MessageObj
                    if (messageObj.id == moreId) {
                        moreId = if (moreId == 100) 0 else moreId + 1

                        if (progressDialog.isShowing) {
                            progressDialog.dismiss()
                        }
                        Toast.makeText(activity, "more fail", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.shouye, container, false)
        init()
        return fragmentView
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(favoriteChangeReceiver)
    }

    private fun init() {

        val swipeRefreshLayout = fragmentView.swiperefreshlayout
        swipeRefreshLayout.setOnRefreshListener {
            //更新逻辑
            refresh()
            swipeRefreshLayout.isRefreshing = false
        }
        fragmentView.titleBar.refreshBtn.setOnClickListener {
            refresh()
        }
        fragmentView.titleBar.moreBtn.setOnClickListener {
            more()
        }
        fragmentView.titleBar.backBtn.setOnClickListener {
            Toast.makeText(activity, "Back", Toast.LENGTH_SHORT).show()
        }

        // 初始化recycler view
        setRecyclerView(_itemList, _bannerList, _favoriteSet)
        // 初始化progress dialog
        progressDialog.apply {
            setMessage("loading...")
            setCancelable(false)
        }
        // 注册broadcast receiver
        val intentFilter = IntentFilter()
        intentFilter.addAction(favoriteIntentFilterAction)
        activity?.registerReceiver(favoriteChangeReceiver, intentFilter)

        // 初始刷新 获取itemList, bannerList, favoriteSet数据
        refresh()
    }

    private fun refresh() {
        progressDialog.show()
        // 本次刷新的id
        val id = refreshId

        // 定时5s后发送失败消息
//        MyApplication.pools.execute {
//            val bMsg = Message()
//            bMsg.what = refreshFail
//            bMsg.obj = MessageObj(id, null)
//            handler.sendMessageDelayed(bMsg, 5000)
//        }

        // 从服务器获取itemList, bannerList, favoriteSet
        MyApplication.pools.execute {
            val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val triple = HttpUtils.refresh(windowManager.defaultDisplay.width, 200)
            val msg = Message()
            if (triple != null) {
                msg.obj = MessageObj(id, RefreshObj(triple.first, triple.second, triple.third))
                msg.what = refreshSuccess
            } else {
                msg.obj = MessageObj(id, null)
                msg.what = refreshFail
            }
            handler.sendMessage(msg)
        }
    }

    private fun more() {
        progressDialog.show()
        // 本次获取更多的id
        val id = moreId

        // 定时5s发送失败消息
        MyApplication.pools.execute {
            val bMsg = Message()
            bMsg.what = moreFail
            bMsg.obj = MessageObj(id, null)
            handler.sendMessageDelayed(bMsg, 5000)
        }

        // 从服务器获取下一页数据
        MyApplication.pools.execute {
            val itemList = HttpUtils.getLists(nextPage)
            val msg = Message()
            if (itemList.isNotEmpty()) {
                msg.obj = MessageObj(id, itemList)
                msg.what = moreSuccess
            } else {
                msg.obj = MessageObj(id, null)
                msg.what = refreshFail
            }
            handler.sendMessage(msg)
        }
    }

    private fun setRecyclerView(itemList: List<ShouyeItem>, bannerList: List<ShouyeBannerFragment>, favoriteSet: MutableSet<String>) {
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        val adapter = ShouyeAdapter(childFragmentManager, itemList, bannerList, favoriteSet)
        recyclerView.adapter = adapter
    }

    private fun notifyRecyclerView(position: Int) {
        recyclerView.adapter?.notifyDataSetChanged()
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPosition(position)
    }

}