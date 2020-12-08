package com.example.android_demo_application.animators

import android.animation.ValueAnimator
import android.util.Log
import android.view.View
import android.widget.ImageButton
import com.example.android_demo_application.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

object MyButtonAnimatorHelper {
    fun addToFavorite(view: View) {
        Log.d("animation", "begin")
        ValueAnimator.ofFloat(2.0f, 0.0f).apply {
            var flag = true
            duration = 1000
            addUpdateListener {
                val value = animatedValue as Float
                if (value > 1.0f) {
                    view.scaleX = value - 1.0f
                    view.scaleY = value - 1.0f
                } else {
                    if (flag) {
                        flag = false
                        if (view is FloatingActionButton) {
                            view.setImageResource(R.drawable.hard_heart)
                        } else if (view is ImageButton) {
                            view.setImageResource(R.drawable.hard_heart)
                        }
                    }
                    view.scaleX = 1.0f - value
                    view.scaleY = 1.0f - value
                }
            }
            start()
        }
    }

    fun removeFromFavorite(view: View) {
        ValueAnimator.ofFloat(2.0f, 0.0f).apply {
            var flag = true
            duration = 1000
            addUpdateListener {
                val value = animatedValue as Float
                if (value > 1.0f) {
                    view.alpha = value - 1.0f
                } else {
                    if (flag) {
                        flag = false
                        if (view is FloatingActionButton) {
                            view.setImageResource(R.drawable.empty_heart)
                        } else if (view is ImageButton) {
                            view.setImageResource(R.drawable.empty_heart)
                        }
                    }
                    view.alpha = 1.0f - value
                }
            }
            start()
        }
    }
}