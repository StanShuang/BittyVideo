package com.stan.video.bittyvideo.mvp.model.bean

import org.litepal.crud.LitePalSupport

/**
 * Created by Stan
 * on 2019/7/4.
 */
data class WatchHistoryBean(val data: HomeBean.Issue.Item) : LitePalSupport() {
    val id: Long = 0
}