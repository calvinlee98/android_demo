package com.example.android_demo_application.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import com.example.android_demo_application.R

class MyButton : FrameLayout {
    lateinit var imageButton1: ImageButton
    lateinit var button: Button
    lateinit var imageButton2: ImageButton

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        //MyButton本身是一个 FrameLayout  构造时  构造出一个view（RelativeLayout  add到自己）
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.mybutton, this)
        imageButton1 = findViewById(R.id.ib)
        button = findViewById(R.id.b)
        imageButton2 = findViewById(R.id.ib2)
    }

    operator fun set(imageSrc: Int, stringSrc: Int) {
        imageButton1.setBackgroundResource(imageSrc)
        button.setText(stringSrc)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return true
    }
}