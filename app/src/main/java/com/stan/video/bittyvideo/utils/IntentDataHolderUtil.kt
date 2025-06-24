package com.stan.video.bittyvideo.utils

import java.lang.ref.WeakReference

/**
 *@Author Stan
 *@Description 数据传输工具类，处理Intent携带大量数据时，超过1MB限制出现的异常场景。
 *@Date 2025/6/10 16:44
 */
object IntentDataHolderUtil {
    private var map = hashMapOf<String, WeakReference<Any>>()

    fun setData(key: String, t: Any) {
        val value = WeakReference(t)
        map[key] = value
    }

    fun <T> getData(key: String): T? {
        val reference = map[key]
        return try {
            reference?.get() as T
        } catch (e: Exception) {
            null
        }
    }

}