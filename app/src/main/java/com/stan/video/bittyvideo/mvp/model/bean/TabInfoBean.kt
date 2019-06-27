package com.stan.video.bittyvideo.mvp.model.bean

/**
 * Created by Stan
 * on 2019/6/27.
 */
data class TabInfoBean(val tabInfo: TabInfo) {
    data class TabInfo(val tabList: ArrayList<Tab>)

    data class Tab(val id: Long, val name: String, val apiUrl: String)
}
