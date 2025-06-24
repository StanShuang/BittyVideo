package com.stan.video.bittyvideo.mvvm.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.mvp.model.bean.CommunityRecommend
import com.stan.video.bittyvideo.view.invisible
import com.stan.video.bittyvideo.view.load
import com.stan.video.bittyvideo.view.visible
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder

/**
 *@Author Stan
 *@Description
 *@Date 2025/6/5 15:24
 */
class BannerAdapter : BaseBannerAdapter<CommunityRecommend.ItemX, BannerAdapter.ViewHolder>() {
    override fun onBind(
        holder: ViewHolder?,
        data: CommunityRecommend.ItemX,
        position: Int,
        pageSize: Int
    ) {
        holder!!.bindData(data, position, pageSize)
    }

    override fun createViewHolder(
        itemView: View?,
        viewType: Int
    ): ViewHolder? {
        return ViewHolder(itemView!!)
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_banner_item_type
    }

    class ViewHolder(val view: View) : BaseViewHolder<CommunityRecommend.ItemX>(view) {
        override fun bindData(
            data: CommunityRecommend.ItemX,
            position: Int,
            pageSize: Int
        ) {
            val ivPicture = findView<ImageView>(R.id.ivPicture)
            val tvLabel = findView<TextView>(R.id.tvLabel)
            if (data.data.label?.text.isNullOrEmpty()) tvLabel.invisible() else tvLabel.visible()
            tvLabel.text = data.data.label?.text ?: ""
            ivPicture.load(data.data.image, 4f)
        }

    }
}