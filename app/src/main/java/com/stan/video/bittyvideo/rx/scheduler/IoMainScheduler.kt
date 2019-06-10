package com.stan.video.bittyvideo.rx.scheduler


import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Stan
 * on 2019/6/10.
 */
class IoMainScheduler<T>: BaseScheduler<T>(Schedulers.io(),AndroidSchedulers.mainThread())