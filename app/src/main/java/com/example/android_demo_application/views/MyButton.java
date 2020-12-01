package com.example.android_demo_application.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.android_demo_application.R;



public class MyButton extends FrameLayout {
    ImageButton imageButton1;
    Button button;
    ImageButton imageButton2;
    public MyButton(Context context) {
        super(context);
    }
    public MyButton(Context context, AttributeSet attrs){
        super(context, attrs);
        //MyButton本身是一个 FrameLayout  构造时  构造出一个view（RelativeLayout  add到自己）
        LayoutInflater.from(context).inflate(R.layout.mybutton,this);
         imageButton1 = findViewById(R.id.ib);
         button = findViewById(R.id.b);
        imageButton2 = findViewById(R.id.ib2);
    }
    public void set(int imageSrc, int stringSrc){
       imageButton1.setBackgroundResource(imageSrc);
       button.setText(stringSrc);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
