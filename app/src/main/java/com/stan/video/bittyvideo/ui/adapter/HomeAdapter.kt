package com.stan.video.bittyvideo.ui.adapter

import android.content.Context
import com.stan.video.bittyvideo.mvp.model.bean.HomeBean
import view.recycleview.ViewHolder
import view.recycleview.adapter.CommonAdapter

/**
 * Created by Stan
 * on 2019/6/13.
 */
class HomeAdapter(context: Context,data: ArrayList<HomeBean.Issue.Item>): CommonAdapter<HomeBean.Issue.Item>(context,data,-1) {
    // banner 作为 RecyclerView 的第一项
    var bannerItemSize = 0
    companion object {
        private const val ITEM_TYPE_BANNER = 1    //Banner 类型
        private const val ITEM_TYPE_TEXT_HEADER = 2   //textHeader
        private const val ITEM_TYPE_CONTENT = 3    //item
    }
    /**
     * 设置 Banner 大小
     */
    fun setBannerSize(count: Int) {
        bannerItemSize = count
    }
    /**
     * 添加更多数据
     */
    fun addItemData(itemList: ArrayList<HomeBean.Issue.Item>) {
        this.mData.addAll(itemList)
        notifyDataSetChanged()
    }
    /**
     * 得到 Item 的类型
     */
    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 ->
                ITEM_TYPE_BANNER
            mData[position + bannerItemSize - 1].type == "textHeader" ->
                ITEM_TYPE_TEXT_HEADER
            else ->
                ITEM_TYPE_CONTENT
        }
    }
    /**
     *  得到 RecyclerView Item 数量（Banner 作为一个 item）
     */
    override fun getItemCount(): Int {
        return when {
            mData.size > bannerItemSize -> mData.size - bannerItemSize + 1
            mData.isEmpty() -> 0
            else -> 1
        }
    }

    override fun bindData(holder: ViewHolder, data: HomeBean.Issue.Item, position: Int) {
    }
}