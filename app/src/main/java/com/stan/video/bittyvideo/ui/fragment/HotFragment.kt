package com.stan.video.bittyvideo.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseFragment
import com.stan.video.bittyvideo.ext.showToast
import com.stan.video.bittyvideo.mvp.contract.HotTabContract
import com.stan.video.bittyvideo.mvp.model.bean.TabInfoBean
import com.stan.video.bittyvideo.mvp.presenter.HotTabPresenter
import com.stan.video.bittyvideo.net.exception.ErrorStatus
import com.stan.video.bittyvideo.view.adapter.BaseFragmentAdapter
import com.stan.video.bittyvideo.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_hot.*

/**
 * Created by Stan
 * on 2019/6/6.
 */
class HotFragment: BaseFragment(),HotTabContract.View {

    override fun getLayoutId(): Int = R.layout.fragment_hot
    private var mTitle: String? = null
    private val mPresenter by lazy { HotTabPresenter() }
    private val mTitleTabText = ArrayList<String>()
    private val fragments = ArrayList<Fragment>()
    companion object {
        fun getInstance(title: String): HotFragment{
            val fragment = HotFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }
    init {
        mPresenter.attachView(this)
    }
    override fun lazyLoad() {
        mPresenter.getTabInfo()
    }

    override fun initView() {
        mLayoutStatusView = multipleStatusView
        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let { StatusBarUtil.setPaddingSmart(it,mTabLayout) }

    }
    override fun showLoading() {
        multipleStatusView.showLoading()
    }

    override fun dismissLoading() {


    }

    override fun setTabInfo(tabInfoBean: TabInfoBean) {
        multipleStatusView.showContent()
        tabInfoBean.tabInfo.tabList.mapTo(mTitleTabText){ it.name}
        tabInfoBean.tabInfo.tabList.mapTo(fragments){ RankFragment.getInstance(it.apiUrl)}
        mViewPager.adapter = BaseFragmentAdapter(childFragmentManager,fragments,mTitleTabText)
        mTabLayout.setupWithViewPager(mViewPager)
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
        mPresenter.detachView()
    }
}