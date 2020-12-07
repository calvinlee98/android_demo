package com.example.android_demo_application.activities

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.android_demo_application.MyApplication
import com.example.android_demo_application.R
import com.example.android_demo_application.fragments.ShouyeFragment
import com.example.android_demo_application.utils.HttpUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private val articleId: String by lazy {
        intent.getStringExtra("articleId")
    }

    // handler
    private val addFavoriteSuccess = 0
    private val addFavoriteFail = 1
    private val removeFavoriteSuccess = 2
    private val removeFavoriteFail = 3
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                addFavoriteSuccess -> {
                    val btn = msg.obj as FloatingActionButton
                    btn.setImageResource(R.drawable.hard_heart)
                    sendFavoriteBroadCast("add")
                }

                addFavoriteFail -> {
                    val btn = msg.obj as FloatingActionButton
                    Toast.makeText(btn.context, "add favorite fail", Toast.LENGTH_SHORT).show()
                }

                removeFavoriteSuccess -> {
                    val btn = msg.obj as FloatingActionButton
                    btn.setImageResource(R.drawable.empty_heart)
                    sendFavoriteBroadCast("remove")
                }

                removeFavoriteFail -> {
                    val btn = msg.obj as FloatingActionButton
                    Toast.makeText(btn.context, "remove favorite fail", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var flag = intent.getBooleanExtra("flag", false)
        if (flag) {
            likeFloatingBtn.setImageResource(R.drawable.hard_heart)
        }

        likeFloatingBtn.setOnClickListener {
            it.isClickable = false
            val message = Message()
            message.obj = likeFloatingBtn
            if (flag) {
                MyApplication.pools.execute {
                    val ret = HttpUtils.cancelLike(articleId)
                    if (ret == "success") {
                        message.what = removeFavoriteSuccess
                        flag = !flag
                    } else {
                        message.what = removeFavoriteFail
                    }
                    handler.sendMessage(message)
                    it.isClickable = true
                }
            } else {
                MyApplication.pools.execute {
                    val ret = HttpUtils.likeArticle(articleId)
                    if (ret == "success") {
                        message.what = addFavoriteSuccess
                        flag = !flag
                    } else {
                        message.what = addFavoriteFail
                    }
                    handler.sendMessage(message)
                    it.isClickable = true
                }
            }
        }

        val url = intent.getStringExtra("url")
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)
    }

    private fun sendFavoriteBroadCast(sFlag: String) {
        val intent = Intent(ShouyeFragment.favoriteIntentFilterAction)
        intent.putExtra("flag", sFlag)
        intent.putExtra("articleId", articleId)
        intent.setPackage(packageName)
        sendBroadcast(intent)
    }
}