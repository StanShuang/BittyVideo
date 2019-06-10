package com.stan.video.bittyvideo.mvp.model


import com.stan.video.bittyvideo.mvp.model.bean.HomeBean
import com.stan.video.bittyvideo.net.RetrofitManager
import com.stan.video.bittyvideo.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by Stan
 * on 2019/6/10.
 */
class HomeModel {
    /**
     * 获取首页 Banner 数据
     */
    fun requestHomeData(num: Int): Observable<HomeBean> {
        return RetrofitManager.serviece.getFirstHomeData(num).compose(SchedulerUtils.ioToMain())
    }
    /**
     * 加载更多
     */
    fun loadMoreData(url:String):Observable<HomeBean>{

        return RetrofitManager.serviece.getMoreHomeData(url)
                .compose(SchedulerUtils.ioToMain())
    }
}