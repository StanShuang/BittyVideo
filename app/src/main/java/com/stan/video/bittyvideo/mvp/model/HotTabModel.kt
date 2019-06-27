package com.stan.video.bittyvideo.mvp.model

import com.stan.video.bittyvideo.mvp.model.bean.TabInfoBean
import com.stan.video.bittyvideo.net.RetrofitManager
import com.stan.video.bittyvideo.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by Stan
 * on 2019/6/27.
 */
class HotTabModel {
    fun getTabInfo(): Observable<TabInfoBean>{
        return RetrofitManager.serviece.getRankList().compose(SchedulerUtils.ioToMain())
    }
}