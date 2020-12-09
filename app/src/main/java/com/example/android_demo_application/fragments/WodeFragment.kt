package com.example.android_demo_application.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.android_demo_application.MyApplication
import com.example.android_demo_application.R
import com.example.android_demo_application.activities.FavoritesActivity
import com.example.android_demo_application.activities.LogActivity
import com.example.android_demo_application.utils.HttpUtils
import com.example.android_demo_application.utils.SharedPreferenceUtils
import com.example.android_demo_application.views.MyButton
import kotlinx.android.synthetic.main.wode.*
import java.lang.ref.WeakReference

/*
* author by @李方正
* 我的 界面的fragment
*
* */   class WodeFragment : Fragment(), View.OnClickListener {

    private lateinit var itemView: View
    private lateinit var myPoints:MyButton
    private lateinit var mySharings:MyButton
    private lateinit var myFavorites:MyButton
    private lateinit var readLater:MyButton
    private lateinit var readHistory:MyButton
    private lateinit var openSourceProject:MyButton
    private lateinit var aboutComposer:MyButton
    private lateinit var settings:MyButton
    private lateinit var goLogin:Button
    private lateinit var logout:Button



    //MyButton extends FrameLayout  是一个FrameLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        itemView = inflater.inflate(R.layout.wode, container, false)
        initView()
        return itemView
    }

    private fun initView() {
         myPoints = itemView.findViewById(R.id.myPoints)
         mySharings = itemView.findViewById(R.id.mySharings)
         myFavorites = itemView.findViewById(R.id.myFavorites)
         readLater = itemView.findViewById(R.id.readLater)
         readHistory  = itemView.findViewById(R.id.readHistory)
        openSourceProject = itemView.findViewById(R.id.openSourceProject)
        aboutComposer = itemView.findViewById(R.id.aboutComposer)
        settings = itemView.findViewById(R.id.settings)
        goLogin = itemView.findViewById(R.id.goLogin)
        logout = itemView.findViewById(R.id.logout)





        goLogin.setOnClickListener { v: View -> onClick(v) }
        logout.setOnClickListener { v: View -> onClick(v) }
        myPoints.setOnClickListener { v: View -> onClick(v) }
        mySharings.setOnClickListener { v: View -> onClick(v) }
        myFavorites.setOnClickListener { v: View -> onClick(v) }
        readLater.setOnClickListener { v: View -> onClick(v) }
        readHistory.setOnClickListener { v: View -> onClick(v) }
        openSourceProject.setOnClickListener { v: View -> onClick(v) }
        aboutComposer.setOnClickListener { v: View -> onClick(v) }
        settings.setOnClickListener { v: View -> onClick(v) }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (MyApplication.isLoggedIn) {
                goLogin.text = MyApplication.userName
                logout.visibility = View.VISIBLE
            } else {
                goLogin.setText(R.string.goLogin)
                logout.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
            if (MyApplication.isLoggedIn) {
                goLogin.text = MyApplication.userName
                logout.visibility = View.VISIBLE
            } else {
                goLogin.setText(R.string.goLogin)
                logout.visibility = View.GONE
            }

    }

    var handler: Handler = MyHandler(this)

    class MyHandler(mFragment:WodeFragment): Handler(Looper.getMainLooper()) {
        private val mFragment:WeakReference<WodeFragment> by lazy { WeakReference<WodeFragment>(mFragment) }
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val s = msg.obj as String
            if (s == "") {
                //没有错误
                //登出
                MyApplication.setIsLoggedIn(false)
                MyApplication.userName = ""
                //清空缓存
                SharedPreferenceUtils.empty()
                mFragment.get()?.logout?.visibility = View.GONE
                mFragment.get()?.goLogin?.setText(R.string.goLogin)
                mFragment.get()?.itemView?.requestLayout()

                val intent = Intent(ShouyeFragment.favoriteIntentFilterAction)
                intent.putExtra("flag", "logout")
                intent.setPackage(mFragment.get()?.activity?.packageName)
                MyApplication.context?.sendBroadcast(intent)
            } else {
                Toast.makeText(MyApplication.context, s, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.goLogin -> {
                val intent = Intent(activity, LogActivity::class.java)
                startActivity(intent)
            }
            R.id.logout ->                 //登出逻辑
                //发送http请求
                //更新UI    登出应该消失
                //删除本地 SharedPreference
                MyApplication.pools.execute {
                    val s = HttpUtils.logout()
                    val message = Message.obtain()
                    message.target = handler
                    message.obj = s
                    handler.sendMessage(message)
                }
            R.id.myPoints -> Toast.makeText(MyApplication.context, "我的积分", Toast.LENGTH_SHORT).show()
            R.id.mySharings -> Toast.makeText(MyApplication.context, "我的分享", Toast.LENGTH_SHORT).show()
            R.id.myFavorites -> {
                val intent1: Intent
                if (MyApplication.isLoggedIn) {
                    intent1 = Intent(activity, FavoritesActivity::class.java)
                    startActivity(intent1)
                } else {
                    intent1 = Intent(activity, LogActivity::class.java)
                    startActivity(intent1)
                }
            }
            R.id.readLater -> Toast.makeText(MyApplication.context, "稍后阅读", Toast.LENGTH_SHORT).show()
            R.id.readHistory -> Toast.makeText(MyApplication.context, "阅读历史", Toast.LENGTH_SHORT).show()
            R.id.openSourceProject -> Toast.makeText(MyApplication.context, "开源项目", Toast.LENGTH_SHORT).show()
            R.id.aboutComposer -> Toast.makeText(MyApplication.context, "关于作者", Toast.LENGTH_SHORT).show()
            R.id.settings -> Toast.makeText(MyApplication.context, "系统设置", Toast.LENGTH_SHORT).show()
        }
    }


}