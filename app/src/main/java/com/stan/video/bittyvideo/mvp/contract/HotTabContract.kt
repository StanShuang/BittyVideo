package com.stan.video.bittyvideo.mvp.contract

import com.stan.video.bittyvideo.base.IBasePresenter
import com.stan.video.bittyvideo.base.IBaseView
import com.stan.video.bittyvideo.mvp.model.bean.TabInfoBean

/**
 * Created by Stan
 * on 2019/6/27.
 */
interface HotTabContract {
    interface View: IBaseView {
        /**
         * 设置 TabInfo
         */
        fun setTabInfo(tabInfoBean: TabInfoBean)

        fun showError(errorMsg:String,errorCode:Int)
    }


    interface Presenter:IBasePresenter<View>{
        /**
         * 获取 TabInfo
         */
        fun getTabInfo()
    }
}