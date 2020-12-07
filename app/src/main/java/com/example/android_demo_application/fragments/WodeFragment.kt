package com.example.android_demo_application.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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
import java.lang.ref.WeakReference

/*
* author by @李方正
* 我的 界面的fragment
*
* */   class WodeFragment : Fragment(), View.OnClickListener {
    var button_logout: Button? = null
    var itemView: View? = null
    var button: Button? = null

    //MyButton extends FrameLayout  是一个FrameLayout
    var wodejifen: MyButton? = null
    var wodefenxiang: MyButton? = null
    var wodeshoucang: MyButton? = null
    var shaohouyuedu: MyButton? = null
    var yuedulishi: MyButton? = null
    var kaiyuanxiangmu: MyButton? = null
    var guanyuzuozhe: MyButton? = null
    var xitongshezhi: MyButton? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        itemView = inflater.inflate(R.layout.wode, container, false)
        initView()
        return itemView
    }

    fun initView() {
        button = itemView!!.findViewById(R.id.qudenglu)
        button_logout = itemView!!.findViewById(R.id.logout)
        wodejifen = itemView!!.findViewById(R.id.wodejifen)
        wodefenxiang = itemView!!.findViewById(R.id.wodefenxiang)
        wodeshoucang = itemView!!.findViewById(R.id.wodeshoucang)
        shaohouyuedu = itemView!!.findViewById(R.id.shaohouyuedu)
        yuedulishi = itemView!!.findViewById(R.id.yuedulishi)
        kaiyuanxiangmu = itemView!!.findViewById(R.id.kaiyuanxiangmu)
        guanyuzuozhe = itemView!!.findViewById(R.id.guanyuzuozhe)
        xitongshezhi = itemView!!.findViewById(R.id.xitongshezhi)


        //设置每个复合button的图片 文字资源
        wodejifen?.set(R.drawable.wodejifen, R.string.wodejifen)
        wodefenxiang?.set(R.drawable.wodefenxiang, R.string.wodefenxiang)
        wodeshoucang?.set(R.drawable.wodeshoucang, R.string.wodeshoucang)
        shaohouyuedu?.set(R.drawable.shaohouyuedu, R.string.shaohouyuedu)
        yuedulishi?.set(R.drawable.yuedulishi, R.string.yuedulishi)
        kaiyuanxiangmu?.set(R.drawable.kaiyuanxiangmu, R.string.kaiyuanxiangmu)
        guanyuzuozhe?.set(R.drawable.guanyuzuozhe, R.string.guanyuzuozhe)
        xitongshezhi?.set(R.drawable.xitongshezhi, R.string.xitongshezhi)
        button?.setOnClickListener { v: View -> onClick(v) }
        button_logout?.setOnClickListener { v: View -> onClick(v) }
        wodejifen?.setOnClickListener { v: View -> onClick(v) }
        wodefenxiang?.setOnClickListener { v: View -> onClick(v) }
        wodeshoucang?.setOnClickListener { v: View -> onClick(v) }
        shaohouyuedu?.setOnClickListener { v: View -> onClick(v) }
        yuedulishi?.setOnClickListener { v: View -> onClick(v) }
        kaiyuanxiangmu?.setOnClickListener({ v: View -> onClick(v) })
        guanyuzuozhe?.setOnClickListener({ v: View -> onClick(v) })
        xitongshezhi?.setOnClickListener({ v: View -> onClick(v) })
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (MyApplication.isLoggedIn) {
                button!!.text = MyApplication.userName
                button_logout!!.visibility = View.VISIBLE
            } else {
                button!!.setText(R.string.qudenglu)
                button_logout!!.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (MyApplication.isLoggedIn) {
            button!!.text = MyApplication.userName
            button_logout!!.visibility = View.VISIBLE
        } else {
            button!!.setText(R.string.qudenglu)
            button_logout!!.visibility = View.GONE
        }
    }

    var handler: Handler = MyHandler(this)

    class MyHandler(fragment: WodeFragment?) : Handler() {
        private val mFragment: WeakReference<WodeFragment>
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
                mFragment.get()?.button_logout!!.visibility = View.GONE
                mFragment.get()?.button!!.setText(R.string.qudenglu)
                mFragment.get()?.itemView!!.requestLayout()
            } else {
                Toast.makeText(MyApplication.context, s, Toast.LENGTH_SHORT).show()
            }
        }

        init {
            mFragment = WeakReference<WodeFragment>(fragment)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.qudenglu -> {
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
            R.id.wodejifen -> Toast.makeText(MyApplication.context, "我的积分", Toast.LENGTH_SHORT).show()
            R.id.wodefenxiang -> Toast.makeText(MyApplication.context, "我的分享", Toast.LENGTH_SHORT).show()
            R.id.wodeshoucang -> {
                val intent1: Intent
                if (MyApplication.isLoggedIn) {
                    intent1 = Intent(activity, FavoritesActivity::class.java)
                    startActivity(intent1)
                } else {
                    intent1 = Intent(activity, LogActivity::class.java)
                    startActivity(intent1)
                }
            }
            R.id.shaohouyuedu -> Toast.makeText(MyApplication.context, "稍后阅读", Toast.LENGTH_SHORT).show()
            R.id.yuedulishi -> Toast.makeText(MyApplication.context, "阅读历史", Toast.LENGTH_SHORT).show()
            R.id.kaiyuanxiangmu -> Toast.makeText(MyApplication.context, "开源项目", Toast.LENGTH_SHORT).show()
            R.id.guanyuzuozhe -> Toast.makeText(MyApplication.context, "关于作者", Toast.LENGTH_SHORT).show()
            R.id.xitongshezhi -> Toast.makeText(MyApplication.context, "系统设置", Toast.LENGTH_SHORT).show()
        }
    }


}