package com.stan.video.bittyvideo.view

import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.stan.video.bittyvideo.app.MyApplication

/**
 *@Author Stan
 *@Description
 *@Date 2025/4/10 14:12
 */
/**
 * 弹出Toast提示。
 *
 * @param duration 显示消息的时间  Either {@link #LENGTH_SHORT} or {@link #LENGTH_LONG}
 */
fun Int.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(MyApplication.context, this, duration).show()
}

/**
 * 解析xml布局
 *
 * @param parent 父布局
 * @param attachToRoot 是否依附到父布局
 */
fun Int.inflate(parent: ViewGroup, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(parent.context).inflate(this, parent, attachToRoot)
}

