package com.stan.video.bittyvideo.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.view.dp2px

/**
 *@Author Stan
 *@Description
 *@Date 2025/6/5 16:44
 */
object DataCalculationUtil {
    /**
     * 通过获取屏幕宽度来计算出每张图片最大的宽度。
     * bothSideSpace: 列表左or右间距
     * middleSpace: 列表中间内间距，左or右。
     *
     * @return 计算后得出的每张图片最大的宽度。
     */
    fun getMaxImageWidth(
        context: Context,
        bothSideSpace: Int = GlobalUtil.getDimension(R.dimen.listSpaceSize),
        middleSpace: Int = dp2px(3f)
    ): Int {
        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val columnWidth = metrics.widthPixels
        return (columnWidth - (bothSideSpace * 2 + middleSpace * 2)) / 2
    }


}