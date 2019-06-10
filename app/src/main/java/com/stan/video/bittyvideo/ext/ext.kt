package com.stan.video.bittyvideo.ext

import android.content.Context
import android.support.v4.app.Fragment
import android.widget.Toast
import com.stan.video.bittyvideo.app.MyApplication

/**
 * Created by Stan
 * on 2019/6/6.
 */
fun Context.showToast(content: String): Toast{
    val toast = Toast.makeText(MyApplication.context,content,Toast.LENGTH_SHORT)
    toast.show()
    return toast
}
fun Fragment.showToast(content: String): Toast{
    val toast = Toast.makeText(this.activity?.applicationContext,content,Toast.LENGTH_SHORT)
    toast.show()
    return toast
}