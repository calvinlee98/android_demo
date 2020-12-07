package com.example.android_demo_application.activities

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.android_demo_application.R
import com.example.android_demo_application.fragments.LoginFragment
import com.example.android_demo_application.fragments.RegisterFragment

class LogActivity : AppCompatActivity() {
    var STATE = 0
    var button: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_log)
        init()
    }

    fun init() {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().add(R.id.fragment_container, LoginFragment()).commit()
        button = findViewById(R.id.button)
        button!!.setOnClickListener {
            val fragmentManager1 = supportFragmentManager
            val transaction = fragmentManager1.beginTransaction()
            if (STATE == LOGIN) {
                //转移到 注册页面
                STATE = REGISTER
                transaction.replace(R.id.fragment_container, RegisterFragment()).commit()
                button!!.setText(R.string.qudenglu)
            } else {
                STATE = LOGIN
                transaction.replace(R.id.fragment_container, LoginFragment()).commit()
                button!!.setText(R.string.quzhuce)
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //如果window上view获取焦点 && view不为空
        if (imm.isActive && currentFocus != null) {
            //拿到view的token 不为空
            if (currentFocus!!.windowToken != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
        return true
    }

    companion object {
        private const val LOGIN = 0
        private const val REGISTER = 1
    }
}