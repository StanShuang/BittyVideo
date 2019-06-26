package com.stan.video.bittyvideo.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseFragment
import com.stan.video.bittyvideo.ext.showToast
import com.stan.video.bittyvideo.mvp.contract.FollowContract
import com.stan.video.bittyvideo.mvp.model.bean.HomeBean
import com.stan.video.bittyvideo.mvp.presenter.FollowPresenter
import com.stan.video.bittyvideo.net.exception.ErrorStatus
import com.stan.video.bittyvideo.ui.adapter.FollowAdapter
import kotlinx.android.synthetic.main.layout_recycleview.*

/**
 * Created by Stan
 * on 2019/6/24.
 */
class FollowFragment: BaseFragment(),FollowContract.View {
    private var mTitle: String? = null
    private var itemLists = ArrayList<HomeBean.Issue.Item>()
    private val mParsenter by lazy { FollowPresenter() }
    private val mAdapter by lazy { activity?.let { FollowAdapter(it,itemLists) } }
    private var loadingMore = false
    override fun getLayoutId(): Int = R.layout.layout_recycleview
    init {
        mParsenter.attachView(this)
    }
    companion object {
        fun getInstance(title: String): FollowFragment{
            val fragment = FollowFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }
    override fun lazyLoad() {
        mParsenter.requestFollowList()

    }

    override fun initView() {
        mRecyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
        }
        //实现自动加载
        mRecyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val itemCount = mRecyclerView.layoutManager.itemCount
                val lastVisibleItem = (mRecyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if(!loadingMore && lastVisibleItem == (itemCount-1)){//滑动到页面可见的最后一个item 为 recycleyview的倒数第二个是加载
                    loadingMore  = true
                    mParsenter.loadMoreData()
                }
            }
        })
        mLayoutStatusView = multipleStatusView

    }
    override fun showLoading() {
        multipleStatusView.showLoading()
    }

    override fun dismissLoading() {
        multipleStatusView.showContent()
    }

    override fun setFollowInfo(issue: HomeBean.Issue) {
        loadingMore = false
        mAdapter?.setData(issue.itemList)
    }

    override fun showError(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if(errorCode == ErrorStatus.NETWORK_ERROR){
            multipleStatusView.showNoNetwork()
        }else{
            multipleStatusView.showError()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mParsenter.detachView()
    }

}