package com.stan.video.bittyvideo.mvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.databinding.FragmentMainContainerBinding
import com.stan.video.bittyvideo.model.bean.TabEntity
import com.stan.video.bittyvideo.utils.GlobalUtil
import com.stan.video.bittyvideo.utils.StatusBarUtil

/**
 *@Author Stan
 *@Description
 *@Date 2025/4/11 15:19
 */
class CommunityFragment : Fragment() {
    private var _binding: FragmentMainContainerBinding? = null
    private val binding: FragmentMainContainerBinding
        get() = _binding!!

    private var mTitle: String? = null

    private val adapter: VpAdapter by lazy {
        VpAdapter(requireActivity()).apply {
            addFragment(
                createFragments
            )
        }
    }

    private val createTitles = ArrayList<CustomTabEntity>().apply {
        this.add(TabEntity(GlobalUtil.getString(R.string.commend)))
        this.add(TabEntity(GlobalUtil.getString(R.string.follow)))
    }

    private val createFragments: Array<Fragment> =
        arrayOf(CommendFragment.newInstance(), FollowFragment.newInstance())

    private var pageChangeCallback: PageChangeCallback? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StatusBarUtil.darkMode(requireActivity())
        StatusBarUtil.setPaddingSmart(requireActivity(), binding.titleBar.flTitleBar)
        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.adapter = adapter
        binding.titleBar.tabLayout.setTabData(createTitles)
        binding.titleBar.tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                binding.viewPager.currentItem = position
            }

            override fun onTabReselect(position: Int) {
            }

        })
        pageChangeCallback = PageChangeCallback(binding.titleBar.tabLayout)
        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback!!)
        binding.titleBar.ivSearch.setOnClickListener {
            SearchFragment.switchFragment(requireActivity())
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        pageChangeCallback?.run { binding.viewPager.unregisterOnPageChangeCallback(this) }
        pageChangeCallback = null
        _binding = null
    }

    class VpAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        private val fragments = mutableListOf<Fragment>()

        fun addFragment(fragment: Array<Fragment>) {
            fragments.addAll(fragment)
        }

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }

    class PageChangeCallback(private val tabLayout: CommonTabLayout) :
        ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            tabLayout.currentTab = position
        }
    }

    companion object {
        fun getInstance(title: String): CommunityFragment {
            val fragment = CommunityFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }
}