package com.stan.video.bittyvideo.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Stan
 * on 2019/6/10.
 */
open class BasePresenter<T: IBaseView>: IBasePresenter<T> {
    var mRootView: T? = null
        private set
    private var compositeDisposable = CompositeDisposable()
    override fun attachView(mRootView: T) {
        this.mRootView = mRootView
    }

    override fun detachView() {
        mRootView = null
        //保证activity结束时取消所有正在执行的订阅
        if(!compositeDisposable.isDisposed){
            compositeDisposable.clear()
        }
    }
    private val isViewAttached: Boolean
        get() = mRootView != null

    fun checkViewAttached() {
        if (!isViewAttached) throw MvpViewNotAttachedException()
    }

    fun addSubscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private class MvpViewNotAttachedException internal constructor() : RuntimeException("Please call IPresenter.attachView(IBaseView) before" + " requesting data to the IPresenter")
}