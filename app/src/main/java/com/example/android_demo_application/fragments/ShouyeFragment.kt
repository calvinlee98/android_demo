package com.example.android_demo_application.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_demo_application.MyApplication
import com.example.android_demo_application.R
import com.example.android_demo_application.fragment_adapters.ShouyeAdapter
import com.example.android_demo_application.utils.BannerUtil
import com.example.android_demo_application.utils.HttpUtils
import com.example.android_demo_application.utities.ShouyeItem
import kotlinx.android.synthetic.main.shouye.view.*
import kotlinx.android.synthetic.main.title_bar.view.*


class ShouyeFragment : Fragment() {
    private val refreshSuccess = 1
    private val refreshFail = 2
    private val moreSuccess = 3
    private val moreFail = 4

    private var nextPage = 1
    private var currRefresh = 0
    private var currMore = 0

    private lateinit var fragmentView: View
    private val progressDialog by lazy {
        ProgressDialog(activity)
    }
    private val recyclerView by lazy {
        fragmentView.shouyeRecyclerView
    }

    private val _itemList = ArrayList<ShouyeItem>()
    private val _bannerList = ArrayList<ShouyeBannerFragment>()

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                refreshSuccess -> {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                    currRefresh = if (currRefresh == 100) 0 else currRefresh + 1
                    recyclerView.removeAllViews()
                    notifyRecyclerView(0)
                }
                refreshFail -> {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                        Toast.makeText(activity, "refresh fail", Toast.LENGTH_SHORT).show()
                    }
                    currRefresh = if (currRefresh == 100) 0 else currRefresh + 1
                }
                moreSuccess -> {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                    currMore = if (currMore == 100) 0 else currMore + 1
                    nextPage += 1
                    notifyRecyclerView(msg.obj as Int)
                }
                moreFail -> {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                        Toast.makeText(activity, "more fail", Toast.LENGTH_SHORT).show()
                    }
                    currMore = if (currMore == 100) 0 else currMore + 1
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

    private fun init() {
        fragmentView.titleBar.refreshBtn.setOnClickListener {
            refresh()
        }

        fragmentView.titleBar.moreBtn.setOnClickListener {
            more()
        }

        fragmentView.titleBar.backBtn.setOnClickListener {
            Toast.makeText(activity, "Back", Toast.LENGTH_SHORT).show()
        }

        setRecyclerView(_itemList, _bannerList)
        progressDialog.apply {
            setMessage("loading...")
            setCancelable(false)
        }

        refresh()
    }

    private fun refresh() {
        showProgressDialog(currRefresh, 0)

        _itemList.clear()
        _bannerList.clear()
        nextPage = 1

        // get first page
        MyApplication.getPools().execute {
            val itemList = HttpUtils.getLists(0)
            val bannerList = BannerUtil.getBannerList()
            _bannerList.addAll(bannerList)
            if (itemList.isNotEmpty()) {
                _itemList.addAll(itemList)
                handler.sendEmptyMessage(refreshSuccess)
            } else {
                handler.sendEmptyMessage(refreshFail)
            }
        }
    }

    private fun more() {
        showProgressDialog(currMore, 1)

        MyApplication.getPools().execute {
            val itemList = HttpUtils.getLists(nextPage)
            val msg = Message()
            if (itemList.isNotEmpty()) {
                msg.what = moreSuccess
                msg.obj = _itemList.size + 1
                _itemList.addAll(itemList)
            } else {
                msg.what = refreshFail
            }
            handler.sendMessage(msg)
        }
    }

    private fun setRecyclerView(itemList: List<ShouyeItem>, bannerList: List<ShouyeBannerFragment>) {
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        val adapter = ShouyeAdapter(childFragmentManager, itemList, bannerList)
        recyclerView.adapter = adapter
    }

    private fun notifyRecyclerView(position: Int) {
        recyclerView.adapter?.notifyItemInserted(position)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        if (position == 0) {
            layoutManager.scrollToPositionWithOffset(position, 0)
        } else {
            layoutManager.scrollToPositionWithOffset(position-1, 0)
        }
    }

    // TODO: bug: when a new task is running(eg, more) and the progressDialog shows again(a new thread), this previous thread will send a refreshFail, which should not happen.
    private fun showProgressDialog(id: Int, type: Int) {
        progressDialog.show()

        // wait for 5s and send fail
        MyApplication.getPools().execute {
            Thread.sleep(5000)
            when (type) {
                0 -> {
                    if (currRefresh == id) {
                        handler.sendEmptyMessage(refreshFail)
                    }
                }
                1 -> {
                    if (currMore == id) {
                        handler.sendEmptyMessage(refreshFail)
                    }
                }
            }
        }
    }
}