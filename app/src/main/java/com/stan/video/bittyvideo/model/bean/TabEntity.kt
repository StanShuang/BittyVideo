package com.stan.video.bittyvideo.model.bean

import android.graphics.drawable.Icon
import com.flyco.tablayout.listener.CustomTabEntity

/**
 * Created by Stan
 * on 2019/6/6.
 */
class TabEntity(var title: String,private var selectIcon: Int,private var unSelectIcon: Int): CustomTabEntity{
    override fun getTabUnselectedIcon(): Int {
       return unSelectIcon
    }

    override fun getTabSelectedIcon(): Int {
        return selectIcon
    }

    override fun getTabTitle(): String {
        return title
    }

}