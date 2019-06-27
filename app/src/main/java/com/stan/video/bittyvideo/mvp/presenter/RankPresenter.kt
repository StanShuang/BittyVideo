package com.stan.video.bittyvideo.mvp.presenter

import com.stan.video.bittyvideo.base.BasePresenter
import com.stan.video.bittyvideo.mvp.contract.RankContract
import com.stan.video.bittyvideo.mvp.model.RankModel
import com.stan.video.bittyvideo.net.exception.ExceptionHandle

/**
 * Created by Stan
 * on 2019/6/27.
 */
class RankPresenter: BasePresenter<RankContract.View>(),RankContract.Presenter {
    private val rankModel by lazy { RankModel() }
    override fun requestRankList(apiUrl: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = rankModel.RequestRankInfo(apiUrl)
                .subscribe({ list ->
                    mRootView?.run {
                        dismissLoading()
                        setRankList(list.itemList)
                    }

                },{ t ->
                    mRootView?.showError(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
                })
        addSubscription(disposable)

    }
}