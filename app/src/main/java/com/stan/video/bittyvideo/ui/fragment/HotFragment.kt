package com.stan.video.bittyvideo.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseFragment

/**
 * Created by Stan
 * on 2019/6/6.
 */
class HotFragment: BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_home
    private var mTitle: String? = null
    companion object {
        fun getInstance(title: String): HotFragment{
            val fragment = HotFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }
    override fun lazyLoad() {

    }

    override fun initView() {

    }


}