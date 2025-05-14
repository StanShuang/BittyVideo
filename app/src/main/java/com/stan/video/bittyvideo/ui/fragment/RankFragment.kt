package com.stan.video.bittyvideo.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseViewFragment
import com.stan.video.bittyvideo.databinding.LayoutRecycleviewBinding
import com.stan.video.bittyvideo.ext.showToast
import com.stan.video.bittyvideo.mvp.contract.RankContract
import com.stan.video.bittyvideo.mvp.model.bean.HomeBean
import com.stan.video.bittyvideo.mvp.presenter.RankPresenter
import com.stan.video.bittyvideo.net.exception.ErrorStatus
import com.stan.video.bittyvideo.ui.adapter.CategoryDetailAdapter


/**
 * Created by Stan
 * on 2019/6/27.
 */
class RankFragment : BaseViewFragment(), RankContract.View {
    private lateinit var binding: LayoutRecycleviewBinding
    private val mPrensenter by lazy { RankPresenter() }
    private val listItem = ArrayList<HomeBean.Issue.Item>()
    private val mAdapter by lazy {
        activity?.let {
            CategoryDetailAdapter(
                it,
                listItem,
                R.layout.item_category_detail
            )
        }
    }
    private var apiUri: String? = null

    companion object {
        fun getInstance(uri: String): RankFragment {
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
        if (!apiUri.isNullOrEmpty()) {
            mPrensenter.requestRankList(apiUri!!)
        }

    }

    override fun initView() {
        mLayoutStatusView = binding.multipleStatusView
        binding.mRecyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter

        }

    }

    override fun getLayoutView(): View {
        binding = LayoutRecycleviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun showLoading() {
        binding.multipleStatusView.showLoading()
    }

    override fun dismissLoading() {

    }

    override fun setRankList(itemList: ArrayList<HomeBean.Issue.Item>) {
        binding.multipleStatusView.showContent()
        mAdapter?.addData(itemList)
    }

    override fun showError(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            binding.multipleStatusView.showNoNetwork()
        } else {
            binding.multipleStatusView.showError()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPrensenter.detachView()
    }
}