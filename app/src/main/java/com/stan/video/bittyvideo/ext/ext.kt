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
fun durationFormat(duration: Long?): String {
    val minute = duration!! / 60
    val second = duration!! % 60
    return if(minute <= 9){
        if(second <= 9){
            "0$minute' 0$second''"
        }else{
            "0$minute' $second''"
        }

    }else{
        if(second <= 9){
            "$minute' 0$second''"
        }else{
            "$minute' $second''"
        }
    }
}