package com.stan.video.bittyvideo.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseFragment
import com.stan.video.bittyvideo.view.TabLayoutHelper
import com.stan.video.bittyvideo.view.adapter.BaseFragmentAdapter
import com.stan.video.bittyvideo.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_hot.*

/**
 * Created by Stan
 * on 2019/6/6.
 */
class DiscoveryFragment: BaseFragment() {
    protected var offscreenPageLimit = 1
    override fun getLayoutId(): Int = R.layout.fragment_hot
    private val tabList = ArrayList<String>()
    private val fragments = ArrayList<Fragment>()
    private var mTitle: String? = null
    companion object {
        fun getInstance(title: String): DiscoveryFragment{
            val fragment = DiscoveryFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }
    override fun lazyLoad() {

    }

    override fun initView() {
        tv_header_title.text = mTitle
        tabList.add(getString(R.string.tablayout_title1))
        tabList.add(getString(R.string.tablayout_title2))
        fragments.add(FollowFragment.getInstance(getString(R.string.tablayout_title1)))
        fragments.add(CategoryFragment.getInstance(getString(R.string.tablayout_title2)))
        mViewPager.offscreenPageLimit = offscreenPageLimit
        mViewPager.adapter = HotFragment.VpAdapter(requireActivity()).apply {
            addFragments(
                fragments
            )
        }
        TabLayoutMediator(mTabLayout, mViewPager) { tab, position ->
            tab.text = tabList[position]
        }.attach()
        //设置下划线长度，可以在tab的选中事件中处理
        TabLayoutHelper.setUpIndicatorWidth(mTabLayout)
        //状态栏透明和间距处理
        //状态栏透明和间距处理
        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let { StatusBarUtil.setPaddingSmart(it, mTabLayout) }


    }

}