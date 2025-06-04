package com.stan.video.bittyvideo.view

import android.view.View
import android.view.animation.AlphaAnimation

/**
 *@Author Stan
 *@Description
 *@Date 2025/4/10 11:10
 */

fun View?.visibleAlphaAnimation(duration: Long = 500L) {
    this?.visibility = View.VISIBLE
    this?.startAnimation(AlphaAnimation(0f, 1f).apply {
        this.duration = duration
        fillAfter = true
    })
}

/**
 * 隐藏view
 */
fun View?.gone() {
    this?.visibility = View.GONE
}

/**
 * 显示view
 */
fun View?.visible() {
    this?.visibility = View.VISIBLE
}

