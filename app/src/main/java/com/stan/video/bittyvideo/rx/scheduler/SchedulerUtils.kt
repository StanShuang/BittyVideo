package com.stan.video.bittyvideo.rx.scheduler

/**
 * Created by Stan
 * on 2019/6/10.
 */
object SchedulerUtils {
    fun <T> ioToMain(): IoMainScheduler<T>{
        return IoMainScheduler()
    }
}