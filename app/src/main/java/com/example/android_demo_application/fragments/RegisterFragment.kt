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
import com.example.android_demo_application.utils.HttpUtils.register
import com.example.android_demo_application.utils.SharedPreferenceUtils.saveUserInfo
import com.google.android.material.textfield.TextInputEditText
import java.lang.ref.WeakReference

class RegisterFragment : Fragment() {
    var itemView: View? = null
    var input_username: TextInputEditText? = null
    var input_email: TextInputEditText? = null
    var input_password: TextInputEditText? = null
    var input_repassword: TextInputEditText? = null
    var button: Button? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        itemView = inflater.inflate(R.layout.fragment_register, container, false)
        init()
        return itemView
    }

    var handler: Handler = MyHandler(this)

    internal class MyHandler(fragment: RegisterFragment) : Handler() {
        var mFragment: WeakReference<RegisterFragment>
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            var fragment: RegisterFragment?
            if (mFragment.get().also { fragment = it } != null) {
                val result = msg.obj as String
                if (result == "") {
                    //成功注册了
                    MyApplication.userName = fragment!!.input_username!!.text.toString()
                    MyApplication.setIsLoggedIn(true)
                    saveUserInfo(fragment!!.input_username!!.text.toString(),
                            fragment!!.input_password!!.text.toString())
                    fragment!!.activity!!.finish()
                } else {
                    Toast.makeText(MyApplication.context, result, Toast.LENGTH_SHORT).show()
                }
            }
        }

        init {
            mFragment = WeakReference(fragment)
        }
    }

    private fun init() {
        input_username = itemView!!.findViewById(R.id.text_input_username)
        input_email = itemView!!.findViewById(R.id.text_input_email)
        input_password = itemView!!.findViewById(R.id.text_input_password)
        input_repassword = itemView!!.findViewById(R.id.text_input_repassword)
        button = itemView!!.findViewById(R.id.button)
        button?.setOnClickListener(View.OnClickListener { v: View? ->
            MyApplication.pools.execute {
                val result = register(input_username?.getText().toString(),
                        input_password?.getText().toString(),
                        input_repassword?.getText().toString())
                val message = Message.obtain()
                message.obj = result
                message.target = handler
                handler.sendMessage(message)
            }
        })
    }
}