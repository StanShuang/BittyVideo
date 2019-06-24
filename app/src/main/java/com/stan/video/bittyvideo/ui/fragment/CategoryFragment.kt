package com.stan.video.bittyvideo.ui.fragment

import android.os.Bundle
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseFragment

/**
 * Created by Stan
 * on 2019/6/24.
 */
class CategoryFragment: BaseFragment() {
    private var mTitle: String? = null
    override fun getLayoutId(): Int = R.layout.fragment_hot
    companion object {
        fun getInstance(title: String): CategoryFragment{
            val fragment = CategoryFragment()
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