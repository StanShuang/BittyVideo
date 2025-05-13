package com.stan.video.bittyvideo.ui.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseViewActivity
import com.stan.video.bittyvideo.databinding.ActivityMainBinding
import com.stan.video.bittyvideo.ext.showToast
import com.stan.video.bittyvideo.model.bean.TabEntity
import com.stan.video.bittyvideo.mvvm.fragment.CommunityFragment
import com.stan.video.bittyvideo.ui.fragment.DiscoveryFragment
import com.stan.video.bittyvideo.ui.fragment.HomeFragment
import com.stan.video.bittyvideo.ui.fragment.HotFragment
import com.stan.video.bittyvideo.ui.fragment.MineFragment

/**
 * Created by Stan
 * on 2019/5/31.
 */
class MainActivity : BaseViewActivity() {
    private lateinit var mTilte: Array<String>
    private lateinit var binding: ActivityMainBinding
    private val mIconUnSelectIds = intArrayOf(
        R.mipmap.ic_home_normal,
        R.mipmap.btn_community_normal,
        R.mipmap.ic_discovery_normal,
        R.mipmap.ic_hot_normal,
        R.mipmap.ic_mine_normal
    )
    private val mIconSelectIds = intArrayOf(
        R.mipmap.ic_home_selected,
        R.mipmap.btn_community_selected,
        R.mipmap.ic_discovery_selected,
        R.mipmap.ic_hot_selected,
        R.mipmap.ic_mine_selected
    )

    override fun layoutView(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mExitTime: Long = 0
    private var mIndex = 0
    private val mTabEntities = ArrayList<CustomTabEntity>()
    private var mHomeFragment: HomeFragment? = null
    private var mCommunityFragment: CommunityFragment? = null
    private var mDiscoveryFragment: DiscoveryFragment? = null
    private var mHotFragment: HotFragment? = null
    private var mMineFragment: MineFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt("currTabIndex")
        }
        super.onCreate(savedInstanceState)
        mTilte = arrayOf(
            applicationContext.resources.getString(R.string.main_home),
            applicationContext.resources.getString(R.string.community),
            applicationContext.resources.getString(R.string.main_discovery),
            applicationContext.resources.getString(R.string.main_hot),
            applicationContext.resources.getString(R.string.main_mine)
        )
        initTab()
        binding.tabLayout.currentTab = mIndex
        switchFragment(mIndex)

    }

    /**
     * 初始化底部菜单
     */
    private fun initTab() {
        (mTilte.indices).mapTo(mTabEntities) {
            TabEntity(mTilte[it], mIconSelectIds[it], mIconUnSelectIds[it])
        }
        //为Tab赋值
        binding.tabLayout.setTabData(mTabEntities)
        binding.tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                //切换fragment
                switchFragment(position)
            }

            override fun onTabReselect(position: Int) {

            }

        })

    }

    /**
     * 切换fragment
     * @param position 下标
     */
    private fun switchFragment(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragment(transaction)
        when (position) {
            0 -> mHomeFragment?.let {
                transaction.show(it)
            } ?: HomeFragment.getInstance(mTilte[position]).let {
                mHomeFragment = it
                transaction.add(R.id.fl_container, it, "home")
            }

            1 -> mCommunityFragment?.let {
                transaction.show(it)
            } ?: CommunityFragment.getInstance(mTilte[position]).let {
                mCommunityFragment = it
                transaction.add(R.id.fl_container, it, "commend")
            }

            2 -> mDiscoveryFragment?.let {
                transaction.show(it)
            } ?: DiscoveryFragment.getInstance(mTilte[position]).let {
                mDiscoveryFragment = it
                transaction.add(R.id.fl_container, it, "discovery")
            }

            3 -> mHotFragment?.let {
                transaction.show(it)
            } ?: HotFragment.getInstance(mTilte[position]).let {
                mHotFragment = it
                transaction.add(R.id.fl_container, it, "hot")
            }

            4 -> mMineFragment?.let {
                transaction.show(it)
            } ?: MineFragment.getInstance(mTilte[position]).let {
                mMineFragment = it
                transaction.add(R.id.fl_container, it, "mine")
            }

            else -> {

            }
        }
        mIndex = position
        binding.tabLayout.currentTab = mIndex
        transaction.commitAllowingStateLoss()

    }

    /**
     * 隐藏所有的fragment
     * @param transaction transaction
     */
    private fun hideFragment(transaction: FragmentTransaction?) {
        mHomeFragment?.let { transaction?.hide(it) }
        mCommunityFragment?.let { transaction?.hide(it) }
        mDiscoveryFragment?.let { transaction?.hide(it) }
        mHotFragment?.let { transaction?.hide(it) }
        mMineFragment?.let { transaction?.hide(it) }
    }

    override fun initData() {

    }

    override fun initView() {

    }

    override fun start() {

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //记录fragment的位置,防止崩溃 activity被系统回收时，fragment错乱
        outState.putInt("currTabIndex", mIndex)

    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime <= 2000) {
                finish()
            } else {
                showToast(resources.getString(R.string.main_quit))
                mExitTime = System.currentTimeMillis()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}