package com.stan.video.bittyvideo.ui.fragment

import android.os.Bundle
import com.stan.video.bittyvideo.base.BaseFragment

/**
 * Created by Stan
 * on 2019/6/6.
 */
class HomeFragment: BaseFragment() {
    private var mTitle: String? = null
    companion object {
        fun getInstance(title: String): HomeFragment{
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun lazyLoad() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}