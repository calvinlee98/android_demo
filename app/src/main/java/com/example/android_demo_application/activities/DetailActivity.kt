package com.example.android_demo_application.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.example.android_demo_application.R
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var flag = intent.getBooleanExtra("flag", false)
        if (flag) {
            likeFloatingBtn.setImageResource(R.drawable.hard_heart)
        }

        val articleId = intent.getStringExtra("articleId")

        likeFloatingBtn.setOnClickListener {
            val intent = Intent("com.example.android_demo.favorite")
            intent.putExtra("articleId", articleId)
            if (flag) {
                likeFloatingBtn.setImageResource(R.drawable.empty_heart)
                intent.putExtra("flag", false)
            } else {
                likeFloatingBtn.setImageResource(R.drawable.hard_heart)
                intent.putExtra("flag", true)
            }
            flag = !flag

            intent.setPackage(packageName)
            sendBroadcast(intent)
        }

        val url = intent.getStringExtra("url")
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)
    }
}