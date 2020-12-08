package com.example.android_demo_application.animators

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.util.Log
import android.view.View
import android.widget.ImageButton
import com.example.android_demo_application.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

object MyButtonAnimatorHelper {
    fun addToFavorite(view: View) {
        val animatorSet = AnimatorSet()

        val animator1 = ValueAnimator.ofFloat(1.0f, 0.0f).apply {
            duration = 500
            addUpdateListener {
                val value = animatedValue as Float
                view.scaleX = value
                view.scaleY = value
                if (value == 0.0f) {
                    if (view is FloatingActionButton) {
                        view.setImageResource(R.drawable.hard_heart)
                    } else if (view is ImageButton) {
                        view.setImageResource(R.drawable.hard_heart)
                    }
                }
            }
        }

        val animator2 = ValueAnimator.ofFloat(0.0f, 1.0f).apply {
            duration = 500
            addUpdateListener {
                val value = animatedValue as Float
                view.scaleX = value
                view.scaleY = value
            }
        }

        animatorSet.playSequentially(animator1, animator2)
        animatorSet.start()

    }

    fun removeFromFavorite(view: View) {
        val animatorSet = AnimatorSet()

        val animator1 = ValueAnimator.ofFloat(1.0f, 0.0f).apply {
            duration = 500
            addUpdateListener {
                val value = animatedValue as Float
                view.alpha = value
                if (value == 0.0f) {
                    if (view is FloatingActionButton) {
                        view.setImageResource(R.drawable.empty_heart)
                    } else if (view is ImageButton) {
                        view.setImageResource(R.drawable.empty_heart)
                    }
                }
            }
        }

        val animator2 = ValueAnimator.ofFloat(0.0f, 1.0f).apply {
            duration = 500
            addUpdateListener {
                val value = animatedValue as Float
                view.alpha = value
            }
        }

        animatorSet.playSequentially(animator1, animator2)
        animatorSet.start()
    }
}