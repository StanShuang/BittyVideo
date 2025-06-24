package com.stan.video.bittyvideo.mvvm.adapter

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.mvp.model.bean.CommunityRecommend
import com.stan.video.bittyvideo.mvvm.fragment.CommendFragment
import com.stan.video.bittyvideo.utils.DataCalculationUtil
import com.stan.video.bittyvideo.view.load

/**
 *@Author Stan
 *@Description
 *@Date 2025/6/5 15:55
 */
class SquareCardOfCommunityContentAdapter(
    val fragment: CommendFragment,
    var dataList: List<CommunityRecommend.ItemX>
) : RecyclerView.Adapter<SquareCardOfCommunityContentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_community_horizontal_scroll_card_itemcollection_item_type,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = dataList[position]
        holder.ivBgPicture.layoutParams.width =
            DataCalculationUtil.getMaxImageWidth(fragment.requireActivity())
        holder.ivBgPicture.load(item.data.bgPicture)
        holder.tvTitle.text = item.data.title
        holder.tvSubTitle.text = item.data.subTitle
        holder.itemView.setOnClickListener {
            //TODO 页面跳转
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivBgPicture: ImageView = view.findViewById(R.id.ivBgPicture)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvSubTitle: TextView = view.findViewById(R.id.tvSubTitle)
    }
}