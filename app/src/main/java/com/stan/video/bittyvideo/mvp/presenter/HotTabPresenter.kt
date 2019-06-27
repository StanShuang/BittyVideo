package com.stan.video.bittyvideo.mvp.presenter

import com.stan.video.bittyvideo.base.BasePresenter
import com.stan.video.bittyvideo.mvp.contract.HotTabContract
import com.stan.video.bittyvideo.mvp.model.HotTabModel
import com.stan.video.bittyvideo.net.exception.ExceptionHandle

/**
 * Created by Stan
 * on 2019/6/27.
 */
class HotTabPresenter: BasePresenter<HotTabContract.View>(),HotTabContract.Presenter {
    private val hotTabModel by lazy { HotTabModel() }
    override fun getTabInfo() {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = hotTabModel.getTabInfo()
                .subscribe({ tabInfo ->
                    mRootView?.run {
                        dismissLoading()
                        setTabInfo(tabInfo)
                    }

                },{ t ->
                    mRootView?.showError(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)

                })
        addSubscription(disposable)
    }
}