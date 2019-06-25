package com.stan.video.bittyvideo.mvp.presenter

import com.stan.video.bittyvideo.base.BasePresenter
import com.stan.video.bittyvideo.mvp.contract.FollowContract
import com.stan.video.bittyvideo.mvp.model.FollowModel
import com.stan.video.bittyvideo.net.exception.ExceptionHandle

/**
 * Created by Stan
 * on 2019/6/24.
 */
class FollowPresenter: BasePresenter<FollowContract.View>(),FollowContract.Presenter {
    private val followModel by lazy { FollowModel() }
    private var nexPageUrl: String? = null
    override fun requestFollowList() {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = followModel.requestFollowList()
                .subscribe({ issue ->
                    mRootView?.apply {
                        dismissLoading()
                        nexPageUrl = issue.nextPageUrl
                        setFollowInfo(issue)
                    }

                },{ throwabel ->
                    mRootView?.apply {
                        showError(ExceptionHandle.handleException(throwabel),ExceptionHandle.errorCode)
                    }

                })
        addSubscription(disposable)
    }

    override fun loadMoreData() {
        val disposabel = nexPageUrl?.let {
            followModel.loadMoreData(it)
                    .subscribe({ issue ->
                        mRootView?.apply {
                            nexPageUrl = issue.nextPageUrl
                            setFollowInfo(issue)
                        }
                    },{ throwable ->
                        mRootView?.apply {
                            showError(ExceptionHandle.handleException(throwable),ExceptionHandle.errorCode)
                        }

                    })
        }
        if(disposabel != null){
            addSubscription(disposabel)
        }
    }
}