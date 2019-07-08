package com.stan.video.bittyvideo.mvp.model

import com.stan.video.bittyvideo.mvp.model.bean.HomeBean
import com.stan.video.bittyvideo.net.RetrofitManager
import com.stan.video.bittyvideo.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by Stan
 * on 2019/7/1.
 */
class SearchModel {
    /**
     * 热门关键词
     */
    fun requestHotWordData(): Observable<ArrayList<String>>{
        return RetrofitManager.serviece.getHotWord().compose(SchedulerUtils.ioToMain())
    }
    /**
     *  关键词返回的结果
     */
    fun getSearchData(words: String): Observable<HomeBean.Issue>{
        return RetrofitManager.serviece.getSearchData(words).compose(SchedulerUtils.ioToMain())
    }
    /**
     * more Data
     */
    fun loadMore(url: String):Observable<HomeBean.Issue>{
        return RetrofitManager.serviece.getIssueData(url).compose(SchedulerUtils.ioToMain())
    }
}