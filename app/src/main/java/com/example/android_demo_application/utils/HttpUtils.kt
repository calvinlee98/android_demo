package com.example.android_demo_application.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.WindowManager
import com.example.android_demo_application.fragments.ShouyeBannerFragment
import com.example.android_demo_application.entities.ShouyeItem
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

object HttpUtils {
     val okHttpClient = OkHttpClient()
    private const val LOGIN_URL = "https://www.wanandroid.com/user/login"
    private const val  REGISTER_URL = "https://www.wanandroid.com/user/register"
    var ARTICLES_LIST = "https://www.wanandroid.com/article/list/"
    var LOGOUT_URL = "https://www.wanandroid.com/user/logout/json"
    var BANNER_URL = "https://www.wanandroid.com/banner/json"
    var FAVORITES_ARTICLES = "https://www.wanandroid.com/lg/collect/list/" //需要拼接  get方法
    var LIKE_ARTICLE = "https://www.wanandroid.com/lg/collect/" // 需要拼接  post方法
    var CANCEL_LIKE = "https://www.wanandroid.com/lg/uncollect_originId/" //需要拼接  post方法
    fun likeArticle(articleId: String): Boolean {
        val requestBody:RequestBody
        val request:Request
        val response:Response
        val responseData:String
        val cookieInfo = "loginUserName=" + SharedPreferenceUtils.savedUserName + ";loginUserPassword=" + SharedPreferenceUtils.savedPassword
        requestBody = FormBody.Builder().build()
        request = Request.Builder().post(requestBody).header("Cookie",cookieInfo).url("$LIKE_ARTICLE$articleId/json").build()
        return try {
            response = okHttpClient.newCall(request).execute()
            responseData = response.body!!.string()
            val jsonObject = JSONObject(responseData)
            jsonObject.getString("errorMsg")==""
        } catch (e: Exception) {
            false
        }
    }

    @JvmStatic
    fun login(username: String?, passoword: String?): String {
        val requestBody:RequestBody
        requestBody = FormBody.Builder().add("username", username!!).add("password", passoword!!).build()
        return postGetErrorMsg(LOGIN_URL, requestBody)
    }

    @JvmStatic
    fun register(username: String?, password: String?, repassword: String?): String {
        val requestBody:RequestBody
        requestBody = FormBody.Builder().add("username", username!!).add("password", password!!).add("repassword", repassword!!).build()
        return postGetErrorMsg(REGISTER_URL, requestBody)
    }

    fun postGetErrorMsg(url: String?, requestBody: RequestBody): String {
        val response:Response
        val responseData:String
        val request:Request = Request.Builder().url(url!!).post(requestBody).build()
        try {
            response = okHttpClient.newCall(request).execute()
            responseData = response.body!!.string()
        } catch (e: IOException) {
            return "error！ retry"
        }
        val jsonObject: JSONObject?
        return try {
            jsonObject = JSONObject(responseData)
            jsonObject.getString("errorMsg")
        } catch (e: JSONException) {
            "格式错误！"
        }
    }

    fun getLists(page: Int): List<ShouyeItem> {
        val request:Request
        val response:Response
        val responseData:String
        val url = "$ARTICLES_LIST$page/json"
        request = Request.Builder().url(url).build()
        return try {
            response = okHttpClient.newCall(request).execute()
            responseData = response.body!!.string()
            val jsonObject = JSONObject(responseData)
            val jsonObject1 = jsonObject.getJSONObject("data")
            val jsonArray = jsonObject1.getJSONArray("datas")
            val list: MutableList<ShouyeItem> = ArrayList()
            for (i in 0 until jsonArray.length()) {
                val `object` = jsonArray.getJSONObject(i)
                list.add(
                    ShouyeItem(
                        `object`.getString("id"),
                        `object`.getString("author"),
                        `object`.getString("publishTime"),
                        `object`.getString("title"),
                        "",
                        `object`.getString("superChapterName"),
                        `object`.getString("link"))
                )
            }
            list
        } catch (e: Exception) {
            ArrayList()
        }
    }

    fun refresh(reqWidth: Int, reqHeight: Int): Triple<List<ShouyeItem>, List<ShouyeBannerFragment>, Set<String>>? {
        val request:Request
        val response:Response
        val responseData:String
        val itemList = getLists(0)
        val bannerList: MutableList<ShouyeBannerFragment> = ArrayList()
        return try {
            request = Request.Builder().url(BANNER_URL).build()
            response = okHttpClient.newCall(request).execute()
            responseData = response.body!!.string()
            val jsonArray = JSONObject(responseData).getJSONArray("data")
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val url = jsonObject.getString("imagePath")
                if (url != "") {
                    val bitmap = getBitmap(url, reqWidth, reqHeight)
                    if (bitmap != null) {
                        bannerList.add(ShouyeBannerFragment(bitmap))
                    }
                }
            }
            Triple(itemList, bannerList, favoritesList)
        } catch (e: Exception) {
            null
        }
    }

    private fun getBitmap(url: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        val request:Request = Request.Builder().url(url).build()
        return try {
            val response = okHttpClient.newCall(request).execute()
            val bytes = response.body!!.bytes()
            ImageLoader.decodeSampledBitmapFromByteArray(bytes, reqWidth, reqHeight)
        } catch (e: Exception) {
            null
        }
    }

    fun logout(): String {
        val response:Response
        val responseData:String
        val request:Request = Request.Builder().url(LOGOUT_URL).build()
        return try {
            response = okHttpClient.newCall(request).execute()
            responseData = response.body!!.string()
            val jsonObject = JSONObject(responseData)
            jsonObject.getString("errorMsg")
        } catch (e: Exception) {
            "登出错误！"
        }
    }

    val favoritesList: Set<String>
        get() {
            var request:Request
            var response:Response
            var responseData:String
            val result: MutableSet<String> = HashSet()
            val cookieInfo = "loginUserName=" + SharedPreferenceUtils.savedUserName + ";loginUserPassword=" + SharedPreferenceUtils.savedPassword
            var page = 0
            while (true) {
                request = Request.Builder().url("$FAVORITES_ARTICLES$page/json").header("Cookie", cookieInfo).build()
                try {
                    response = okHttpClient.newCall(request!!).execute()
                    responseData = response!!.body!!.string()
                    val jsonObject = JSONObject(responseData)
                    val jsonObject1 = jsonObject.getJSONObject("data")
                    val jsonArray = jsonObject1.getJSONArray("datas")
                    if (jsonArray.length() == 0) break
                    for (i in 0 until jsonArray.length()) {
                        val `object` = jsonArray.getJSONObject(i)
                        result.add(`object`.getString("originId"))
                    }
                    page++
                } catch (e: Exception) {
                    return result
                }
            }
            return result
        }

    fun getFavorites(page: Int): List<ShouyeItem> {
        val request:Request
        val response:Response
        val responseData:String
        val cookieInfo = "loginUserName=" + SharedPreferenceUtils.savedUserName + ";loginUserPassword=" + SharedPreferenceUtils.savedPassword
        request = Request.Builder().url("$FAVORITES_ARTICLES$page/json").header("Cookie", cookieInfo).build()
        return try {
            response = okHttpClient.newCall(request).execute()
            responseData = response.body!!.string()
            val jsonObject = JSONObject(responseData)
            val jsonObject1 = jsonObject.getJSONObject("data")
            val jsonArray = jsonObject1.getJSONArray("datas")
            val list: MutableList<ShouyeItem> = ArrayList()
            for (i in 0 until jsonArray.length()) {
                val `object` = jsonArray.getJSONObject(i)
                list.add(
                    ShouyeItem(
                        `object`.getString("originId"),
                        `object`.getString("author"),
                        `object`.getString("publishTime"),
                        `object`.getString("title"),
                        "",
                        `object`.getString("chapterName"),
                        `object`.getString("link"))
                )
            }
            list
        } catch (e: Exception) {
            ArrayList()
        }
    }

    fun cancelLike(page_id: String): Boolean {
        val requestBody:RequestBody
        val request:Request
        val response:Response
        val responseData:String
        val url = "$CANCEL_LIKE$page_id/json"
        val cookieInfo = "loginUserName=" + SharedPreferenceUtils.savedUserName + ";loginUserPassword=" + SharedPreferenceUtils.savedPassword
        requestBody = FormBody.Builder().build()
        request = Request.Builder().url(url).post(requestBody).header("Cookie", cookieInfo).build()
        return try {
            response = okHttpClient.newCall(request).execute()
            responseData = response.body!!.string()
            Log.d("TAG", responseData)
            JSONObject(responseData).getString("errorMsg")==""
        } catch (e: Exception) {
            false
        }
    }
}