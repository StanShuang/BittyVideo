package com.stan.video.bittyvideo.mvp.presenter

import com.stan.video.bittyvideo.base.BasePresenter
import com.stan.video.bittyvideo.mvp.contract.SearchContract
import com.stan.video.bittyvideo.mvp.model.SearchModel
import com.stan.video.bittyvideo.net.exception.ExceptionHandle

/**
 * Created by Stan
 * on 2019/7/1.
 */
class SearchPresenter:BasePresenter<SearchContract.View>(),SearchContract.Presenter {
    private val searchModel by lazy { SearchModel() }
    private var nextPageUrl: String? = null
    override fun requestHotWordData() {
        checkViewAttached()
        mRootView?.apply {
            closeSoftKeyboard()
            showLoading()
        }
        addSubscription(disposable = searchModel.requestHotWordData()
                .subscribe({ string ->
                    mRootView?.apply {
                        dismissLoading()
                        setHotWordData(string)
                    }

                },{ throwable ->
                    mRootView?.showError(ExceptionHandle.handleException(throwable),ExceptionHandle.errorCode)

                }))
    }

    override fun querySearchData(words: String) {
        checkViewAttached()
        mRootView?.apply {
            closeSoftKeyboard()
            showLoading()
        }
        addSubscription(disposable = searchModel.getSearchData(words)
                .subscribe({ issue ->
                    mRootView?.apply {
                        dismissLoading()
                        if(issue.count>0 && issue.itemList.size >0){
                            nextPageUrl = issue.nextPageUrl
                            setSearchResult(issue)
                        }else{
                            setEmptyView()
                        }
                    }

                },{ t ->
                    mRootView?.apply {
                        dismissLoading()
                        showError(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
                    }

                }))
    }

    override fun loadMoreData() {
        checkViewAttached()
        nextPageUrl?.let {
            addSubscription(disposable = searchModel.loadMore(it)
                    .subscribe({ issue ->
                        mRootView?.apply {
                            nextPageUrl = issue.nextPageUrl
                            setSearchResult(issue)
                        }
                    },{ t ->
                        mRootView?.showError(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
                    }))
        }
    }
}