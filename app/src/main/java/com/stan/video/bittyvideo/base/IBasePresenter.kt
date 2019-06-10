package com.stan.video.bittyvideo.base

/**
 * Created by Stan
 * on 2019/6/10.
 */
interface IBasePresenter<in V: IBaseView> {
    fun attachView(mRootView: V)
    fun detachView()
}