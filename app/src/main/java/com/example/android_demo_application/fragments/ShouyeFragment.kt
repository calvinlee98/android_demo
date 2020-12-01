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
import androidx.recyclerview.widget.RecyclerView
import com.example.android_demo_application.R
import com.example.android_demo_application.fragment_adapters.ShouyeAdapter
import com.example.android_demo_application.fragment_adapters.ShouyeItem
import kotlinx.android.synthetic.main.shouye.view.*
import kotlinx.android.synthetic.main.title_bar.view.*
import kotlin.concurrent.thread

class ShouyeFragment : Fragment() {
    private val loadingSuccess = 1
    private val loadingFail = 2

    private lateinit var fragmentView: View
    private lateinit var progressDialog: ProgressDialog

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                loadingSuccess -> {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                        setRecyclerView()
                    }
                }
                loadingFail -> {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                        Toast.makeText(activity, "fail!", Toast.LENGTH_SHORT).show()
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
        progressDialog = ProgressDialog(activity)

        fragmentView.titleBar.refreshBtn.setOnClickListener {
            refresh()
        }

        fragmentView.titleBar.moreBtn.setOnClickListener {
            Toast.makeText(activity, "More", Toast.LENGTH_SHORT).show()
        }

        fragmentView.titleBar.backBtn.setOnClickListener {
            Toast.makeText(activity, "Back", Toast.LENGTH_SHORT).show()
        }

        refresh()
    }

    private fun refresh() {
        progressDialog.apply {
            setMessage("loading...")
            setCancelable(false)
            show()
        }

        // wait for 5s and send fail
        thread {
            Thread.sleep(5000)
            val msg = Message()
            msg.what = loadingFail
            handler.sendMessage(msg)
        }

        thread {
            getWebResources()
        }

    }

    private fun getWebResources() {
        // TODO: get web resources and change it to loading success!
        val msg = Message()
        msg.what = loadingSuccess
        handler.sendMessage(msg)
    }

    private fun setRecyclerView() {
        val recyclerView = fragmentView.shouyeRecyclerView

        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        val adapter = ShouyeAdapter(childFragmentManager, getArticles())
        recyclerView.adapter = adapter
    }

    private fun getArticles() : List<ShouyeItem> {
        return listOf(ShouyeItem("k1t", "2020/11/30", "Test", "Test", "Test", "Test"))
    }
}