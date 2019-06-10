package com.stan.video.bittyvideo.ui.fragment

import android.os.Bundle
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseFragment
import com.stan.video.bittyvideo.ext.showToast
import com.stan.video.bittyvideo.mvp.contract.HomeContract
import com.stan.video.bittyvideo.mvp.model.bean.HomeBean
import com.stan.video.bittyvideo.mvp.presenter.HomePresenter
import com.stan.video.bittyvideo.net.exception.ErrorStatus
import com.stan.video.bittyvideo.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by Stan
 * on 2019/6/6.
 */
class HomeFragment: BaseFragment() ,HomeContract.View{
    private var mTitle: String? = null
    override fun getLayoutId(): Int = R.layout.fragment_home
    private val mPresenter: HomePresenter by lazy { HomePresenter() }
    //是否是在下拉刷新
    private var isRefresh = false
    private var num: Int = 1
    companion object {
        fun getInstance(title: String): HomeFragment{
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun lazyLoad() {
        mPresenter.requestHomeData(num)
    }

    override fun initView() {
        mPresenter.attachView(this)

        //状态栏透明和间距处理
        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let { StatusBarUtil.setPaddingSmart(it, toolbar) }
    }

    /**
     * 下拉刷新时不展示loading
     */
    override fun showLoading() {
        if(!isRefresh){
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }

    }

    override fun dismissLoading() {
      mRefreshLayout.finishRefresh()
    }

    override fun setHomeData(homeBean: HomeBean) {

    }

    override fun setMoreData(itemList: ArrayList<HomeBean.Issue.Item>) {

    }

    override fun showError(msg: String, errorCode: Int) {
        showToast(msg)
        if(errorCode == ErrorStatus.NETWORK_ERROR){
            mLayoutStatusView?.showNoNetwork()
        }else{
            mLayoutStatusView?.showError()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}