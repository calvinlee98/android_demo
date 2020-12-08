package com.example.android_demo_application.fragments

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
import com.example.android_demo_application.presenter.LoginPresenter
import com.example.android_demo_application.utils.HttpUtils.login
import com.example.android_demo_application.utils.SharedPreferenceUtils
import com.google.android.material.textfield.TextInputEditText
import java.lang.ref.WeakReference

class LoginFragment : Fragment(),LoginView {
    private val presenter:LoginPresenter by lazy { LoginPresenter() }
    private lateinit var itemView:View
    var yonghuming: TextInputEditText? = null
    var mima: TextInputEditText? = null
    var button: Button? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        presenter.attachView(this)
        itemView = inflater.inflate(R.layout.fragment_login, container, false)
        init()
        return itemView
    }
    private fun init(){
        yonghuming = itemView.findViewById(R.id.text_intput_username)
        mima = itemView.findViewById(R.id.text_input_password)
        button = itemView.findViewById(R.id.denglu)
        button?.setOnClickListener{
            presenter.login(yonghuming?.text.toString(),mima?.text.toString())
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun onSuccess() {
        SharedPreferenceUtils.saveUserInfo(yonghuming!!.text.toString(), mima!!.text.toString())
        MyApplication.setIsLoggedIn(true)
        MyApplication.userName = yonghuming!!.text.toString()

        //发送广播
        var intent = Intent("login")
        intent.setPackage(activity?.packageName)
        activity?.sendBroadcast(intent)
        activity?.finish()
    }

    override fun onFail(s: String?) {
        MyApplication.setIsLoggedIn(false)
        MyApplication.userName = ""
        //清空 SharedPreference
        SharedPreferenceUtils.empty()
        Toast.makeText(MyApplication.context, s, Toast.LENGTH_SHORT).show()
    }
}