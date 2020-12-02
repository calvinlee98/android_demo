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

    private var nextPage = 1

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
                    recyclerView.removeAllViews()
                    notifyRecyclerView(0)
                }
                refreshFail -> {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                        Toast.makeText(activity, "fail!", Toast.LENGTH_SHORT).show()
                    }
                }
                moreSuccess -> {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                    nextPage += 1
                    notifyRecyclerView(msg.obj as Int)
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
        showProgressDialog()

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
            }
        }
    }

    private fun more() {
        showProgressDialog()

        MyApplication.getPools().execute {
            val itemList = HttpUtils.getLists(nextPage)
            if (itemList.isNotEmpty()) {
                val msg = Message()
                msg.what = moreSuccess
                msg.obj = _itemList.size + 1
                _itemList.addAll(itemList)
                handler.sendMessage(msg)
            }
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
        layoutManager.scrollToPositionWithOffset(position, 0)
    }

    // TODO: bug: when a new task is running(eg, more) and the progressDialog shows again(a new thread), this previous thread will send a refreshFail, which should not happen.
    private fun showProgressDialog() {
        progressDialog.show()

        // wait for 5s and send fail
        MyApplication.getPools().execute {
            Thread.sleep(5000)
            if (progressDialog.isShowing) {
                val msg = Message()
                msg.what = refreshFail
                handler.sendMessage(msg)
            }
        }
    }
}