package com.stan.video.bittyvideo.model.bean

import android.graphics.drawable.Icon
import com.flyco.tablayout.listener.CustomTabEntity

/**
 * Created by Stan
 * on 2019/6/6.
 */
class TabEntity(var title: String,private var selectIcon: Int = 0,private var unSelectIcon: Int = 0): CustomTabEntity{
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