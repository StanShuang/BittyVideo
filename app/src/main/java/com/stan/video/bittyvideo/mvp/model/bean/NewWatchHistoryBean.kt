package com.stan.video.bittyvideo.mvp.model.bean

import org.litepal.crud.LitePalSupport

/**
 * Author: Stan
 * Date: 2020/4/28 16:39
 * Description:
 */
class NewWatchHistoryBean(val title: String,
                          val category: String,
                          val duration: Long,
                          val detail: String,
                          val feed: String,
                          val playInfo: ArrayList<HomeBean.Issue.Item.Data.PlayInfo>
) : LitePalSupport() {
    val id: Long = 0
}