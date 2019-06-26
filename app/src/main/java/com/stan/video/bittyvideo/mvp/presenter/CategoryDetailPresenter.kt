package com.stan.video.bittyvideo.mvp.presenter

import com.stan.video.bittyvideo.base.BasePresenter
import com.stan.video.bittyvideo.mvp.contract.CategoryDetailContract
import com.stan.video.bittyvideo.mvp.model.CategoryDetailModel
import com.stan.video.bittyvideo.net.exception.ExceptionHandle

/**
 * Created by Stan
 * on 2019/6/26.
 */
class CategoryDetailPresenter: BasePresenter<CategoryDetailContract.View>(),CategoryDetailContract.Presenter {
    private val categoryDetailModel by lazy { CategoryDetailModel() }
    private var nextPageNext: String? = null
    override fun getCategoryDetailList(id: Long) {
        checkViewAttached()
        val disposable = categoryDetailModel.getCategoryDetailList(id)
                .subscribe({ issue ->
                    mRootView?.run {
                        nextPageNext = issue.nextPageUrl
                        setCateDetailList(issue.itemList)
                    }

                },{ t ->
                    mRootView?.showError(t.toString())

                })
        addSubscription(disposable)
    }

    override fun loadMoreData() {
        val disposable = nextPageNext?.let {
            categoryDetailModel.loadMoreData(it)
                    .subscribe({ issuse ->
                        mRootView?.run {
                            nextPageNext = issuse.nextPageUrl
                            setCateDetailList(issuse.itemList)
                        }

                    },{ t ->
                        mRootView?.showError(t.toString())

                    })
        }
        if(disposable != null){
            addSubscription(disposable)
        }
    }
}