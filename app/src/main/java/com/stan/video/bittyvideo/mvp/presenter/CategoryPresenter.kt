package com.stan.video.bittyvideo.mvp.presenter

import com.stan.video.bittyvideo.base.BasePresenter
import com.stan.video.bittyvideo.mvp.contract.CategoryContract
import com.stan.video.bittyvideo.mvp.model.CategoryModel
import com.stan.video.bittyvideo.net.exception.ExceptionHandle

/**
 * Created by Stan
 * on 2019/6/26.
 */
class CategoryPresenter: BasePresenter<CategoryContract.View>(),CategoryContract.Presenter {
    private val categoryModel by lazy { CategoryModel() }

    override fun getCategoryData() {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = categoryModel.getCategoryData()
                .subscribe({ categoryList ->
                    mRootView?.apply {
                        dismissLoading()
                        showCategory(categoryList)
                    }

                },{ t ->
                    mRootView?.apply {
                        showError(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
                    }

                })
        addSubscription(disposable)
    }
}