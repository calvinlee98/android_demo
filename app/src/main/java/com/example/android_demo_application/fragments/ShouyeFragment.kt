package com.example.android_demo_application.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_demo_application.MyApplication
import com.example.android_demo_application.R
import com.example.android_demo_application.fragment_adapters.ShouyeAdapter
import com.example.android_demo_application.utils.HttpUtils
import com.example.android_demo_application.utities.ShouyeItem
import kotlinx.android.synthetic.main.shouye.*
import kotlinx.android.synthetic.main.shouye.view.*
import kotlinx.android.synthetic.main.title_bar.view.*
import kotlin.concurrent.thread


class ShouyeFragment : Fragment() {
    private val loadingSuccess = 1
    private val loadingFail = 2
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

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                loadingSuccess -> {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                    setRecyclerView(_itemList)
                }
                loadingFail -> {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                    Toast.makeText(activity, "fail!", Toast.LENGTH_SHORT).show()
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

        refresh()
    }

    private fun refresh() {
        showProgressDialog()

        // get first page and set recyclerView
        val pool = MyApplication.getPools()
        pool.execute {
            val itemList = HttpUtils.getLists(0)
            val msg = Message()
            if (itemList.isNotEmpty()) {
                msg.what = loadingSuccess
                _itemList.addAll(itemList)
            } else {
                msg.what = loadingFail
            }
            handler.sendMessage(msg)
        }
    }

    private fun more() {
        showProgressDialog()

        val pool = MyApplication.getPools()
        pool.execute {
            val itemList = HttpUtils.getLists(nextPage)
            val msg = Message()
            if (itemList.isNotEmpty()) {
                msg.what = moreSuccess
                msg.obj = _itemList.size
                _itemList.addAll(itemList)
            } else {
                msg.what = loadingFail
            }
            handler.sendMessage(msg)
        }
    }

    private fun setRecyclerView(itemList: List<ShouyeItem>) {
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        val adapter = ShouyeAdapter(childFragmentManager, itemList, listOf(ShouyeBannerFragment()))
        recyclerView.adapter = adapter
    }

    private fun notifyRecyclerView(position: Int) {
        recyclerView.adapter?.notifyItemInserted(position)
        recyclerView.scrollToPosition(position)
    }

    private fun showProgressDialog() {
        progressDialog.apply {
            setMessage("loading...")
            setCancelable(false)
            show()
        }

        // wait for 5s and send fail
        MyApplication.getPools().execute {
            Thread.sleep(5000)
            if (progressDialog.isShowing) {
                val msg = Message()
                msg.what = loadingFail
                handler.sendMessage(msg)
            }
        }
    }
}