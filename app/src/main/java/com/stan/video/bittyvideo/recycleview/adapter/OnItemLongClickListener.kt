package com.stan.video.bittyvideo.recycleview.adapter

/**
 * Created by Stan
 * on 2019/6/13.
 * Adapter条目的长按事件
 */
interface OnItemLongClickListener {
    fun onItemLongClick(obj: Any?, position: Int): Boolean
}