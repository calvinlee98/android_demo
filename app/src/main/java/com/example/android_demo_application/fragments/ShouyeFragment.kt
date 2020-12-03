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
import com.example.android_demo_application.utils.HttpUtils
import com.example.android_demo_application.utities.ShouyeItem
import kotlinx.android.synthetic.main.shouye.view.*
import kotlinx.android.synthetic.main.title_bar.view.*


class ShouyeFragment : Fragment() {
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

    // message obj
    inner class MessageObj (val id: Int, val obj: Any?)

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

                        _itemList.clear()
                        _bannerList.clear()
                        nextPage = 1

                        val pair = messageObj.obj as Pair<List<ShouyeItem>, List<ShouyeBannerFragment>>
                        _itemList.addAll(pair.first)
                        _bannerList.addAll(pair.second)

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
        progressDialog.show()
        val id = refreshId

        // get first page
        MyApplication.getPools().execute {
            val pair = HttpUtils.refresh()
            val msg = Message()
            if (pair != null) {
                msg.obj = MessageObj(id, pair)
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

        val id = moreId
        MyApplication.getPools().execute {
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

    private fun setRecyclerView(itemList: List<ShouyeItem>, bannerList: List<ShouyeBannerFragment>) {
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        val adapter = ShouyeAdapter(childFragmentManager, itemList, bannerList)
        recyclerView.adapter = adapter
    }

    private fun notifyRecyclerView(position: Int) {
//        recyclerView.adapter?.notifyItemInserted(position)
        recyclerView.adapter?.notifyDataSetChanged()
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPosition(position)
    }

}