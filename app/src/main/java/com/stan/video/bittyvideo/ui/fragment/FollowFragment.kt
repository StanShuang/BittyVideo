package com.stan.video.bittyvideo.ui.fragment

import android.os.Bundle
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseFragment
import com.stan.video.bittyvideo.ext.showToast
import com.stan.video.bittyvideo.mvp.contract.FollowContract
import com.stan.video.bittyvideo.mvp.model.bean.HomeBean
import com.stan.video.bittyvideo.mvp.presenter.FollowPresenter
import com.stan.video.bittyvideo.net.exception.ErrorStatus
import kotlinx.android.synthetic.main.layout_recycleview.*

/**
 * Created by Stan
 * on 2019/6/24.
 */
class FollowFragment: BaseFragment(),FollowContract.View {
    private var mTitle: String? = null
    private val mParsenter by lazy { FollowPresenter() }
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

    }
    override fun showLoading() {
        multipleStatusView.showLoading()
    }

    override fun dismissLoading() {
        multipleStatusView.showContent()
    }

    override fun setFollowInfo(issue: HomeBean.Issue) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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