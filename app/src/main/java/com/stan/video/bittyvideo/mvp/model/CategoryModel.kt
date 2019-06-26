package com.stan.video.bittyvideo.mvp.model

import com.stan.video.bittyvideo.mvp.model.bean.CategoryBean
import com.stan.video.bittyvideo.net.RetrofitManager
import com.stan.video.bittyvideo.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by Stan
 * on 2019/6/26.
 */
class CategoryModel {
    fun getCategoryData(): Observable<ArrayList<CategoryBean>>{
        return RetrofitManager.serviece.getCategory().compose(SchedulerUtils.ioToMain())
    }
}