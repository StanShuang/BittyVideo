package com.stan.video.bittyvideo.view

import com.stan.video.bittyvideo.app.MyApplication

/**
 *@Author Stan
 *@Description
 *@Date 2025/4/10 11:20
 */

/**
 * 根据手机的分辨率将dp转成为px。
 */
fun dp2px(dp: Float): Int {
    val scale = MyApplication.context.resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

/**
 * 根据手机的分辨率将px转成dp。
 */
fun px2dp(px: Float): Int {
    val scale = MyApplication.context.resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}