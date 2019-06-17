package com.stan.video.bittyvideo.mvp.model

import com.stan.video.bittyvideo.mvp.model.bean.HomeBean
import com.stan.video.bittyvideo.net.RetrofitManager
import com.stan.video.bittyvideo.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by Stan
 * on 2019/6/17.
 */
class VideoDetailModel{
    fun requestRelatedData(id:Long): Observable<HomeBean.Issue> {

        return RetrofitManager.serviece.getRelatedData(id)
                .compose(SchedulerUtils.ioToMain())
    }
}