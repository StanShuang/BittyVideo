package com.stan.video.bittyvideo.mvp.contract

import com.stan.video.bittyvideo.base.IBasePresenter
import com.stan.video.bittyvideo.base.IBaseView
import com.stan.video.bittyvideo.mvp.model.bean.HomeBean

/**
 * Created by Stan
 * on 2019/6/27.
 */
interface RankContract {
    interface View: IBaseView {
        /**
         * 设置排行榜的数据
         */
        fun setRankList(itemList: ArrayList<HomeBean.Issue.Item>)

        fun showError(errorMsg:String,errorCode:Int)
    }


    interface Presenter:IBasePresenter<View>{
        /**
         * 获取 TabInfo
         */
        fun requestRankList(apiUrl:String)
    }
}