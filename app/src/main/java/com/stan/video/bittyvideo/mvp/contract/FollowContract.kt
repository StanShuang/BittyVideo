package com.stan.video.bittyvideo.mvp.contract

import com.stan.video.bittyvideo.base.IBasePresenter
import com.stan.video.bittyvideo.base.IBaseView
import com.stan.video.bittyvideo.mvp.model.bean.HomeBean

/**
 * Created by Stan
 * on 2019/6/24.
 */
interface FollowContract {
    interface View : IBaseView {
        /**
         * 设置关注信息数据
         */
        fun setFollowInfo(issue: HomeBean.Issue)

        fun showError(errorMsg: String, errorCode: Int)
    }


    interface Presenter : IBasePresenter<View> {
        /**
         * 获取List
         */
        fun requestFollowList()

        /**
         * 加载更多
         */
        fun loadMoreData()
    }
}