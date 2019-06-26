package com.stan.video.bittyvideo.mvp.contract

import com.stan.video.bittyvideo.base.IBasePresenter
import com.stan.video.bittyvideo.base.IBaseView
import com.stan.video.bittyvideo.mvp.model.bean.HomeBean

/**
 * Created by Stan
 * on 2019/6/26.
 */
interface CategoryDetailContract {
    interface View: IBaseView {
        /**
         *  设置列表数据
         */
        fun setCateDetailList(itemList:ArrayList<HomeBean.Issue.Item>)

        fun showError(errorMsg:String)




    }

    interface Presenter:IBasePresenter<View>{

        fun getCategoryDetailList(id:Long)

        fun loadMoreData()
    }
}