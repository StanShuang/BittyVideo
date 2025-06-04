package com.stan.video.bittyvideo.utils

import android.view.View
import com.stan.video.bittyvideo.app.MyApplication

/**
 *@Author Stan
 *@Description
 *@Date 2025/4/10 14:59
 */
object GlobalUtil {
    /**
     * 获取资源文件中定义的字符串。
     *
     * @param resId
     * 字符串资源id
     * @return 字符串资源id对应的字符串内容。
     */
    fun getString(resId: Int): String = MyApplication.context.resources.getString(resId)

    /**
     * 批量设置控件点击事件。
     *
     * @param v 点击的控件
     * @param block 处理点击事件回调代码块
     */
    fun setOnClickListener(vararg v: View?, block: View.() -> Unit) {
        val listener = View.OnClickListener { it.block() }
        v.forEach { it?.setOnClickListener(listener) }
    }
}