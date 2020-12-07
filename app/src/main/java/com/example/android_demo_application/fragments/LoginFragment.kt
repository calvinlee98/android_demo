package com.example.android_demo_application.fragments

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
import com.example.android_demo_application.utils.HttpUtils.login
import com.example.android_demo_application.utils.SharedPreferenceUtils
import com.google.android.material.textfield.TextInputEditText
import java.lang.ref.WeakReference

class LoginFragment : Fragment() {
    private class MyHandler(fragment: LoginFragment?) : Handler() {
        var weakReference: WeakReference<LoginFragment?> = WeakReference(fragment)
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (weakReference.get() != null) {
                val s = msg.obj as String
                if (s == "") {
                    //保存 登录逻辑
                    SharedPreferenceUtils.saveUserInfo(weakReference.get()?.yonghuming!!.text.toString(), weakReference.get()!!.mima!!.text.toString())
                    MyApplication.setIsLoggedIn(true)
                    MyApplication.userName = weakReference.get()?.yonghuming!!.text.toString()
                    weakReference.get()?.activity!!.finish()
                    return
                }
                MyApplication.setIsLoggedIn(false)
                MyApplication.userName = ""
                SharedPreferenceUtils.empty()
                Toast.makeText(MyApplication.context, s, Toast.LENGTH_SHORT).show()
            }
        }

    }

    var yonghuming: TextInputEditText? = null
    var mima: TextInputEditText? = null
    var button: Button? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        yonghuming = view.findViewById(R.id.text_intput_username)
        mima = view.findViewById(R.id.text_input_password)
        button = view.findViewById(R.id.denglu)
        val handler: Handler = MyHandler(this)
        button!!.setOnClickListener {
            MyApplication.pools.execute {
                val string = login(yonghuming?.text.toString(), mima?.text.toString())
                val message = Message.obtain()
                message.target = handler
                message.obj = string
                handler.sendMessage(message)
            }
        }
        return view
    }
}