package com.stan.video.bittyvideo.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseFragment
import com.stan.video.bittyvideo.ext.showToast
import com.stan.video.bittyvideo.mvp.contract.RankContract
import com.stan.video.bittyvideo.mvp.model.bean.HomeBean
import com.stan.video.bittyvideo.mvp.presenter.RankPresenter
import com.stan.video.bittyvideo.net.exception.ErrorStatus
import com.stan.video.bittyvideo.ui.adapter.CategoryDetailAdapter
import kotlinx.android.synthetic.main.layout_recycleview.*


/**
 * Created by Stan
 * on 2019/6/27.
 */
class RankFragment: BaseFragment(),RankContract.View {

    private val mPrensenter by lazy { RankPresenter() }
    private val listItem = ArrayList<HomeBean.Issue.Item>()
    private val mAdapter by lazy { activity?.let { CategoryDetailAdapter(it,listItem,R.layout.item_category_detail) } }
    private var apiUri: String? = null
    companion object {
        fun getInstance(uri: String): RankFragment{
            val fragment = RankFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.apiUri = uri
            return fragment

        }
    }
    init {
        mPrensenter.attachView(this)
    }
    override fun lazyLoad() {
        if(!apiUri.isNullOrEmpty()){
            mPrensenter.requestRankList(apiUri!!)
        }

    }

    override fun initView() {
        mLayoutStatusView = multipleStatusView
        mRecyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter

        }

    }

    override fun getLayoutId(): Int  = R.layout.layout_recycleview
    override fun showLoading() {
        multipleStatusView.showLoading()
    }

    override fun dismissLoading() {

    }

    override fun setRankList(itemList: ArrayList<HomeBean.Issue.Item>) {
        multipleStatusView.showContent()
        mAdapter?.addData(itemList)
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
        mPrensenter.detachView()
    }
}