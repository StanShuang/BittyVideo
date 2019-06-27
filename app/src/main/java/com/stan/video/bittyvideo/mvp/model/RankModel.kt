package com.stan.video.bittyvideo.mvp.model

import com.stan.video.bittyvideo.mvp.model.bean.HomeBean
import com.stan.video.bittyvideo.net.RetrofitManager
import com.stan.video.bittyvideo.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by Stan
 * on 2019/6/27.
 */
class RankModel {
    fun RequestRankInfo(url: String): Observable<HomeBean.Issue>{
        return RetrofitManager.serviece.getIssueData(url).compose(SchedulerUtils.ioToMain())
    }
}