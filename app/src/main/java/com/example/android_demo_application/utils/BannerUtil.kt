package com.example.android_demo_application.utils

import android.graphics.BitmapFactory
import androidx.fragment.app.Fragment
import com.example.android_demo_application.fragments.ShouyeBannerFragment
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.lang.Exception

object BannerUtil {
    private val client: OkHttpClient by lazy {
        OkHttpClient()
    }

    const val BANNER_URL = "https://www.wanandroid.com/banner/json"

    fun getBannerList(): List<ShouyeBannerFragment> {
        val bannerList = ArrayList<ShouyeBannerFragment>()
        val urlList = getImageUrl()
        try {
            for (url in urlList) {
                val request = Request.Builder()
                        .url(url)
                        .build()
                val response = client.newCall(request).execute()
                val bytes = response.body?.bytes()
                if (bytes != null) {
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    bannerList.add(ShouyeBannerFragment(bitmap))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bannerList
    }

    private fun getImageUrl(): List<String> {
        var urlList = listOf<String>()
        try {
            val request = Request.Builder()
                    .url(BANNER_URL)
                    .build()
            val response = client.newCall(request).execute()
            val jsonData = response.body?.string()
            if (jsonData != null) {
                urlList = parseJSON(jsonData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return urlList
    }

    private fun parseJSON(jsonData: String): List<String> {
        val urlList = ArrayList<String>()
        try {
            val jsonArray = JSONObject(jsonData).getJSONArray("data")
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val url = jsonObject.getString("imagePath")
                if (url.isNotEmpty()) {
                    urlList.add(url)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return urlList
    }
}