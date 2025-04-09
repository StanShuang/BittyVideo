package com.stan.video.bittyvideo.view.adapter

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by Stan
 * on 2019/6/24.
 * 该类内的每一个生成的 Fragment 都将保存在内存之中，
 * 因此适用于那些相对静态的页，数量也比较少的那种；
 * 如果需要处理有很多页，并且数据动态性较大、占用内存较多的情况，
 * 应该使用FragmentStatePagerAdapter。
 */
class BaseFragmentAdapter: FragmentPagerAdapter {
    private var fragmentList: List<Fragment>? = ArrayList()
    private var mTitles: List<String>? = null
    constructor(fm: FragmentManager, fragemntList: List<Fragment>) : super(fm){
        this.fragmentList = fragmentList
    }
    constructor(fm: FragmentManager, fragemntList: List<Fragment>, mTitles: List<String>): super(fm){
        this.mTitles = mTitles
        setFragment(fm,fragemntList,mTitles)
    }
    //刷新fragment
    @SuppressLint("CommitTransaction")
    private fun setFragment(fm: FragmentManager, fragemntList: List<Fragment>, mTitles: List<String>) {
        this.mTitles = mTitles
        if(this.fragmentList != null){
            val ft = fm.beginTransaction()
            fragmentList?.forEach {
                ft.remove(it)
            }
            ft?.commitAllowingStateLoss()
            fm.executePendingTransactions()
        }
        this.fragmentList = fragemntList
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList!![position]
    }

    override fun getCount(): Int {
        return fragmentList!!.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if(mTitles != null) mTitles!![position] else ""
    }
}