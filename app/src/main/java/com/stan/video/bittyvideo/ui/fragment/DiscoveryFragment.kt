package com.stan.video.bittyvideo.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseViewFragment
import com.stan.video.bittyvideo.databinding.FragmentHotBinding
import com.stan.video.bittyvideo.view.TabLayoutHelper
import com.stan.video.bittyvideo.utils.StatusBarUtil

/**
 * Created by Stan
 * on 2019/6/6.
 */
class DiscoveryFragment: BaseViewFragment() {
    private lateinit var binding: FragmentHotBinding
    protected var offscreenPageLimit = 1
    override fun getLayoutView(): View {
        binding = FragmentHotBinding.inflate(layoutInflater)
        return binding.root
    }
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
        binding.tvHeaderTitle.text = mTitle
        tabList.add(getString(R.string.tablayout_title1))
        tabList.add(getString(R.string.tablayout_title2))
        fragments.add(FollowFragment.getInstance(getString(R.string.tablayout_title1)))
        fragments.add(CategoryFragment.getInstance(getString(R.string.tablayout_title2)))
        binding.mViewPager.offscreenPageLimit = offscreenPageLimit
        binding.mViewPager.adapter = HotFragment.VpAdapter(requireActivity()).apply {
            addFragments(
                fragments
            )
        }
        TabLayoutMediator(binding.mTabLayout, binding.mViewPager) { tab, position ->
            tab.text = tabList[position]
        }.attach()
        //设置下划线长度，可以在tab的选中事件中处理
        TabLayoutHelper.setUpIndicatorWidth(binding.mTabLayout)
        //状态栏透明和间距处理
        //状态栏透明和间距处理
        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let { StatusBarUtil.setPaddingSmart(it, binding.mTabLayout) }


    }

}