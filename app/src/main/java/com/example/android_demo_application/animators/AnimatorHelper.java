package com.example.android_demo_application.animators;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import com.example.android_demo_application.R;

public class AnimatorHelper {
    public static void playSecondAnimator(View view){
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view,"scaleX",1.0f,0.0f).setDuration(500);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view,"scaleY",1.0f,0.0f).setDuration(500);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animator1).with(animator2);
        animatorSet.start();
        view.setBackgroundResource(R.drawable.hard_heart);
        animator1 = ObjectAnimator.ofFloat(view,"scaleX",0.0f,1.0f).setDuration(500);
        animator2 = ObjectAnimator.ofFloat(view,"scaleY",0.0f,1.0f).setDuration(500);
        animatorSet = new AnimatorSet();
        animatorSet.play(animator1).with(animator2);
        animatorSet.start();


    }
    public static void playFirstAnimator(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"alpha",1.0f,0.0f).setDuration(500);
        animator.start();
        view.setBackgroundResource(R.drawable.empty_heart);
        animator = ObjectAnimator.ofFloat(view,"alpha",0.0f,1.0f).setDuration(500);
        animator.start();
    }
}
