package com.stan.video.bittyvideo.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.stan.video.bittyvideo.base.BaseViewFragment
import com.stan.video.bittyvideo.databinding.FragmentHotBinding
import com.stan.video.bittyvideo.ext.showToast
import com.stan.video.bittyvideo.mvp.contract.HotTabContract
import com.stan.video.bittyvideo.mvp.model.bean.TabInfoBean
import com.stan.video.bittyvideo.mvp.presenter.HotTabPresenter
import com.stan.video.bittyvideo.net.exception.ErrorStatus
import com.stan.video.bittyvideo.utils.StatusBarUtil

/**
 * Created by Stan
 * on 2019/6/6.
 */
class HotFragment : BaseViewFragment(), HotTabContract.View {
    private lateinit var binding: FragmentHotBinding
    protected var offscreenPageLimit = 1
    override fun getLayoutView(): View {
        binding = FragmentHotBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mTitle: String? = null
    private val mPresenter by lazy { HotTabPresenter() }
    private val mTitleTabText = ArrayList<String>()
    private val fragments = ArrayList<Fragment>()

    companion object {
        fun getInstance(title: String): HotFragment {
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
        mLayoutStatusView = binding.multipleStatusView
        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let { StatusBarUtil.setPaddingSmart(it, binding.mTabLayout) }

    }

    override fun showLoading() {
        binding.multipleStatusView.showLoading()
    }

    override fun dismissLoading() {


    }

    override fun setTabInfo(tabInfoBean: TabInfoBean) {
        binding.multipleStatusView.showContent()
        tabInfoBean.tabInfo.tabList.mapTo(mTitleTabText) { it.name }
        tabInfoBean.tabInfo.tabList.mapTo(fragments) { RankFragment.getInstance(it.apiUrl) }
        binding.mViewPager.offscreenPageLimit = offscreenPageLimit
        binding.mViewPager.adapter = VpAdapter(requireActivity()).apply {
            addFragments(
                fragments
            )
        }
        TabLayoutMediator(binding.mTabLayout, binding.mViewPager) { tab, position ->
            tab.text = mTitleTabText[position]
        }.attach()
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
        mPresenter.detachView()
    }

    class VpAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        private val fragments = mutableListOf<Fragment>()
        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

        fun addFragments(fragment: ArrayList<Fragment>) {
            fragments.addAll(fragment)
        }
    }

}