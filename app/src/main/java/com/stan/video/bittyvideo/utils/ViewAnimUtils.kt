package com.stan.video.bittyvideo.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * Created by Stan
 * on 2019/6/27.
 */
object ViewAnimUtils {
    interface OnRevealAnimationListener{
        fun onRevealHide()
        fun onRevealShow()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun animatRevelshow(context: Context, view: View,
                        startRadius: Int, @ColorRes color: Int,
                        listener: OnRevealAnimationListener){
        val cx = (view.left + view.right) / 2
        val cy = (view.top + view.bottom) /2
        val finalRadius = Math.hypot(view.width.toDouble(),view.height.toDouble()).toFloat()
        // 设置圆形显示动画
        val anim = ViewAnimationUtils.createCircularReveal(view,cx,cy,startRadius.toFloat(),finalRadius)
        anim.duration = 300
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.addListener(object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                view.visibility = View.VISIBLE
                listener.onRevealShow()
            }

            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                view.setBackgroundColor(ContextCompat.getColor(context,color))
            }
        })

    anim.start()
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun animateRevealHide(context: Context, view: View,
                          finalRadius: Int, @ColorRes color: Int,
                          listener: OnRevealAnimationListener){
        val cx = (view.left + view.right) / 2
        val cy = (view.top + view.bottom) / 2
        val initialRadius = view.width
        val anim = ViewAnimationUtils.createCircularReveal(view,cx,cy,initialRadius.toFloat(),finalRadius.toFloat())
        anim.duration = 300
        anim.interpolator =AccelerateDecelerateInterpolator()
        anim.addListener(object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                view.visibility = View.VISIBLE
                listener.onRevealHide()
            }

            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                view.setBackgroundColor(ContextCompat.getColor(context,color))
            }

        })
        anim.start()

    }
}