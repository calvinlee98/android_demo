package com.example.android_demo_application.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.example.android_demo_application.R
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val flag = intent.getBooleanExtra("flag", false)
        if (flag) {
            likeFloatingBtn.setImageResource(R.drawable.hard_heart)
        }

        likeFloatingBtn.setOnClickListener {

        }

        val url = intent.getStringExtra("url")
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)
    }
}