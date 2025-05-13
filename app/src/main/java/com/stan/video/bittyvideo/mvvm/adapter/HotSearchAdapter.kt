package com.stan.video.bittyvideo.mvvm.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stan.video.bittyvideo.BuildConfig
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.utils.Const
import com.stan.video.bittyvideo.utils.GlobalUtil
import com.stan.video.bittyvideo.utils.showToast
import com.stan.video.bittyvideo.view.inflate

/**
 *@Author Stan
 *@Description 热搜关键词Adapter
 *@Date 2025/4/10 14:21
 */
class HotSearchAdapter(private val dataList: List<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return dataList.size + 1
    }

    override fun getItemViewType(position: Int) = when (position) {
        0 -> Const.ItemViewType.CUSTOM_HEADER
        else -> HOT_SEARCH_TYPE
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Const.ItemViewType.CUSTOM_HEADER -> HeaderViewHolder(
                R.layout.item_search_header.inflate(
                    parent
                )
            )

            Const.ItemViewType.MAX -> HotSearchViewHolder(R.layout.item_search.inflate(parent))
            else -> null!!
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.tvTitle.text = GlobalUtil.getString(R.string.hot_keywords)
            }
            is HotSearchViewHolder -> {
                val item = dataList[position - 1]
                holder.tvKeywords.text = item
                holder.itemView.setOnClickListener {
                    "${item},${GlobalUtil.getString(R.string.currently_not_supported)}".showToast()
                }
            }
            else -> {
                holder.itemView.visibility = View.GONE
                if (BuildConfig.DEBUG) "${TAG}:${Const.Toast.BIND_VIEWHOLDER_TYPE_WARN}\n${holder}".showToast()
            }
        }
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)!!
    }

    class HotSearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvKeywords = view.findViewById<TextView>(R.id.tvKeywords)!!
    }


    companion object {
        const val TAG = "HotSearchAdapter"
        const val HOT_SEARCH_TYPE = Const.ItemViewType.MAX
    }

}
