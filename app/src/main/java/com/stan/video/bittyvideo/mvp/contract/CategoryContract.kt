package com.stan.video.bittyvideo.mvp.contract

import com.stan.video.bittyvideo.base.IBasePresenter
import com.stan.video.bittyvideo.base.IBaseView
import com.stan.video.bittyvideo.mvp.model.bean.CategoryBean

/**
 * Created by Stan
 * on 2019/6/26.
 */
interface CategoryContract {
    interface View : IBaseView {
        /**
         * 显示分类的信息
         */
        fun showCategory(categoryList: ArrayList<CategoryBean>)

        /**
         * 显示错误信息
         */
        fun showError(errorMsg:String,errorCode:Int)
    }

    interface Presenter:IBasePresenter<View>{
        /**
         * 获取分类的信息
         */
        fun getCategoryData()
    }
}