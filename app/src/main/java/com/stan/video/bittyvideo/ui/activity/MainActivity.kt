package com.stan.video.bittyvideo.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.view.KeyEvent
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseActivity
import com.stan.video.bittyvideo.ext.showToast
import com.stan.video.bittyvideo.model.bean.TabEntity
import com.stan.video.bittyvideo.ui.fragment.DiscoveryFragment
import com.stan.video.bittyvideo.ui.fragment.HomeFragment
import com.stan.video.bittyvideo.ui.fragment.HotFragment
import com.stan.video.bittyvideo.ui.fragment.MineFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Stan
 * on 2019/5/31.
 */
class MainActivity: BaseActivity() {
    private lateinit var mTilte : Array<String>
    private val mIconUnSelectIds = intArrayOf(R.mipmap.ic_home_normal,R.mipmap.ic_discovery_normal,R.mipmap.ic_hot_normal,R.mipmap.ic_mine_normal)
    private val mIconSelectIds = intArrayOf(R.mipmap.ic_home_selected,R.mipmap.ic_discovery_selected,R.mipmap.ic_hot_selected,R.mipmap.ic_mine_selected)
    override fun layoutId(): Int = R.layout.activity_main
    private var mExitTime: Long = 0
    private var mIndex = 0
    private val mTabEntities = ArrayList<CustomTabEntity>()
    private var mHomeFragment: HomeFragment? = null
    private var mDiscoveryFragment: DiscoveryFragment? = null
    private var mHotFragment: HotFragment? = null
    private var mMineFragment: MineFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        if(savedInstanceState != null){
            mIndex = savedInstanceState.getInt("currTabIndex")
        }
        super.onCreate(savedInstanceState)
        mTilte = arrayOf(
                applicationContext.resources.getString(R.string.main_home),
                applicationContext.resources.getString(R.string.main_discovery),
                applicationContext.resources.getString(R.string.main_hot),
                applicationContext.resources.getString(R.string.main_mine)
        )
        initTab()
        tab_layout.currentTab = mIndex
        switchFragment(mIndex)

    }

    /**
     * 初始化底部菜单
     */
    private fun initTab() {
        (mTilte.indices).mapTo(mTabEntities){
            TabEntity(mTilte[it],mIconSelectIds[it],mIconUnSelectIds[it])
        }
        //为Tab赋值
        tab_layout.setTabData(mTabEntities)
        tab_layout.setOnTabSelectListener(object : OnTabSelectListener{
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
        when(position){
            0 -> mHomeFragment?.let{
                transaction.show(it)
            } ?: HomeFragment.getInstance(mTilte[position]).let{
                mHomeFragment = it
                transaction.add(R.id.fl_container,it,"home")
            }
            1 -> mDiscoveryFragment?.let{
                transaction.show(it)
            } ?: DiscoveryFragment.getInstance(mTilte[position]).let{
                mDiscoveryFragment = it
                transaction.add(R.id.fl_container,it,"discovery")
            }
            2 -> mHotFragment?.let{
                transaction.show(it)
            } ?: HotFragment.getInstance(mTilte[position]).let{
                mHotFragment = it
                transaction.add(R.id.fl_container,it,"hot")
            }
            3 -> mMineFragment?.let{
                transaction.show(it)
            } ?: MineFragment.getInstance(mTilte[position]).let{
                mMineFragment = it
                transaction.add(R.id.fl_container,it,"mine")
            }
            else -> {

            }
        }
        mIndex = position
        tab_layout.currentTab = mIndex
        transaction.commitAllowingStateLoss()

    }

    /**
     * 隐藏所有的fragment
     * @param transaction transaction
     */
    private fun hideFragment(transaction: FragmentTransaction?) {
        mHomeFragment?.let {transaction?.hide(it)}
        mDiscoveryFragment?.let{transaction?.hide(it)}
        mHotFragment?.let{transaction?.hide(it)}
        mMineFragment?.let{transaction?.hide(it)}
    }

    override fun initData() {

    }

    override fun initView() {

    }

    override fun start() {

    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle?) {
//        super.onSaveInstanceState(outState)
        //记录fragment的位置,防止崩溃 activity被系统回收时，fragment错乱
        if (tab_layout != null) {
            outState?.putInt("currTabIndex", mIndex)
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis() - mExitTime <= 2000){
                finish()
            }else{
                showToast(resources.getString(R.string.main_quit))
                mExitTime = System.currentTimeMillis()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}