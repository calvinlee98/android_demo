package com.example.android_demo_application.animators

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.android_demo_application.R
import com.example.android_demo_application.entities.ShouyeItem

object AnimatorHelper {

    //喜欢文章的动画
    fun playSecondAnimator(view: View) {
        var animator = ValueAnimator.ofFloat(1.0f,0.0f)
        animator.duration = 500
        animator.addUpdateListener {
            val value:Float = it.animatedValue as Float
            view.scaleX = value
            view.scaleY = value
            if(value == 0.0f)
                view.setBackgroundResource(R.drawable.hard_heart)
        }
        animator.start()

        animator = ValueAnimator.ofFloat(0.0f,1.0f)
        animator.duration = 500
        animator.addUpdateListener {
              val value:Float = it.animatedValue as Float
              view.scaleX   = value
               view.scaleY = value
        }
        animator.start()

    }

    //取消收藏的动画
    fun playFirstAnimator(
        view: View,
        adapter: RecyclerView.Adapter<*>,
        list: MutableList<ShouyeItem>,
        position: Int
    ) {
        val animator = ValueAnimator.ofFloat(1.0f, 0.0f)
        animator.duration = 500
        animator.addUpdateListener { animation: ValueAnimator ->
            val `val` = animation.animatedValue as Float
            view.alpha = `val`
            if (`val` == 0.0f) view.setBackgroundResource(R.drawable.empty_heart)
        }
        val animator1 = ValueAnimator.ofFloat(0.0f, 1.0f)
        animator1.duration = 500
        animator1.addUpdateListener { animation: ValueAnimator ->
            val `val` = animation.animatedValue as Float
            view.alpha = `val`
            if (`val` == 1.0f) {
                list.removeAt(position)
                adapter.notifyItemRemoved(position)
                adapter.notifyDataSetChanged()
            }
        }
        val set = AnimatorSet()
        set.play(animator1).after(animator)
        set.start()
    }
}