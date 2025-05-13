package com.stan.video.bittyvideo.utils

import android.widget.Toast
import com.stan.video.bittyvideo.app.MyApplication

/**
 *@Author Stan
 *@Description
 *@Date 2025/4/10 15:03
 */

/**
 * 弹出Toast提示。
 *
 * @param duration 显示消息的时间  Either {@link #LENGTH_SHORT} or {@link #LENGTH_LONG}
 */
fun CharSequence.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(MyApplication.context, this, duration).show()
}