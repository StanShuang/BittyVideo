package com.stan.video.bittyvideo.utils

import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

/**
 *@Author Stan
 *@Description 时间和日期工具类。
 *@Date 2025/5/21 16:40
 */

object DateUtil {
    fun getDate(dateMillis: Long, pattern: String = "yyyy-MM-dd"): String =
        SimpleDateFormat(pattern, Locale.getDefault()).format(
            Date(dateMillis)
        )
}
