package com.stan.video.bittyvideo.mvvm.adapter

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import com.stan.video.bittyvideo.BuildConfig
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.mvp.model.bean.CommunityRecommend
import com.stan.video.bittyvideo.mvvm.activity.UgcDetailActivity
import com.stan.video.bittyvideo.mvvm.adapter.decoration.SquareCardOfCommunityContentItemDecoration
import com.stan.video.bittyvideo.mvvm.fragment.CommendFragment
import com.stan.video.bittyvideo.utils.Const
import com.stan.video.bittyvideo.utils.DataCalculationUtil
import com.stan.video.bittyvideo.utils.GlobalUtil
import com.stan.video.bittyvideo.utils.showToast
import com.stan.video.bittyvideo.view.EmptyViewHolder
import com.stan.video.bittyvideo.view.VideoListener
import com.stan.video.bittyvideo.view.dp2px
import com.stan.video.bittyvideo.view.gone
import com.stan.video.bittyvideo.view.inflate
import com.stan.video.bittyvideo.view.invisible
import com.stan.video.bittyvideo.view.load
import com.stan.video.bittyvideo.view.setDrawable
import com.stan.video.bittyvideo.view.visible
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder
import de.hdodenhof.circleimageview.CircleImageView

/**
 *@Author Stan
 *@Description
 *@Date 2025/5/21 17:18
 */
class CommendAdapter(val fragment: CommendFragment) :
    PagingDataAdapter<CommunityRecommend.Item, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item?.type) {
            STR_HORIZONTAL_SCROLLCARD_TYPE -> {
                when (item.data.dataType) {
                    STR_ITEM_COLLECTION_DATA_TYPE -> HORIZONTAL_SCROLLCARD_ITEM_COLLECTION_TYPE
                    STR_HORIZONTAL_SCROLLCARD_DATA_TYPE -> HORIZONTAL_SCROLLCARD_TYPE
                    else -> Const.ItemViewType.UNKNOWN
                }
            }

            STR_COMMUNITY_COLUMNS_CARD -> {
                if (item.data.dataType == STR_FOLLOW_CARD_DATA_TYPE) FOLLOW_CARD_TYPE
                else Const.ItemViewType.UNKNOWN
            }

            else -> Const.ItemViewType.UNKNOWN
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            HORIZONTAL_SCROLLCARD_ITEM_COLLECTION_TYPE -> {
                HorizontalScrollCardItemCollectionViewHolder(
                    R.layout.item_community_horizontal_scrollcard_item_collection_type.inflate(
                        parent
                    )
                )
            }

            HORIZONTAL_SCROLLCARD_TYPE -> {
                HorizontalScrollCardViewHolder(
                    R.layout.item_community_horizontal_scrollcard_type.inflate(
                        parent
                    )
                )
            }

            FOLLOW_CARD_TYPE -> {
                FollowCardViewHolder(
                    R.layout.item_community_columns_card_follow_card_type.inflate(
                        parent
                    )
                )
            }

            else -> EmptyViewHolder(View(parent.context))
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val item = getItem(position)!!
        when (holder) {
            is HorizontalScrollCardItemCollectionViewHolder -> {
                (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
                    true
                holder.recyclerView.layoutManager =
                    LinearLayoutManager(fragment.requireActivity()).apply {
                        orientation = LinearLayoutManager.HORIZONTAL
                    }
                //如果没有装饰器
                if (holder.recyclerView.itemDecorationCount == 0) {
                    holder.recyclerView.addItemDecoration(SquareCardOfCommunityContentItemDecoration())
                }
                holder.recyclerView.adapter =
                    SquareCardOfCommunityContentAdapter(fragment, item.data.itemList)
            }

            is HorizontalScrollCardViewHolder -> {
                (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
                    true
                holder.bannerViewPager.run {
                    setCanLoop(true)
                    setRoundCorner(dp2px(4f))
                    setRevealWidth(0, 14)
                    if (item.data.itemList.size == 1) setPageMargin(0) else setPageMargin(dp2px(4f))
                    setIndicatorVisibility(View.GONE)
                    removeDefaultPageTransformer()
                    setAdapter(BannerAdapter())
                    setOnPageClickListener { position ->
                        //TODO 点击逻辑
                    }
                    create(item.data.itemList)
                }
            }

            is FollowCardViewHolder -> {
                holder.tvChoice.gone()
                holder.ivPlay.gone()
                holder.ivLayers.gone()
                if (item.data.content.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) holder.tvChoice.visible()
                if ((item.data.header?.iconType ?: "".trim()) == "round") {
                    holder.ivAvatar.invisible()
                    holder.ivRoundAvatar.visible()
                    holder.ivRoundAvatar.load(item.data.content.data.owner.avatar)
                } else {
                    holder.ivAvatar.visible()
                    holder.ivRoundAvatar.gone()
                    holder.ivAvatar.load(item.data.content.data.owner.avatar)
                }
                holder.ivBgPicture.run {
                    val imageHeight = calculateImageHeight(
                        item.data.content.data.width,
                        item.data.content.data.height
                    )
                    layoutParams.width =
                        DataCalculationUtil.getMaxImageWidth(fragment.requireActivity())
                    layoutParams.height = imageHeight
                    this.load(item.data.content.data.cover.feed, 4f)
                }
                holder.tvCollectionCount.text =
                    item.data.content.data.consumption.collectionCount.toString()
                val drawable = ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.ic_favorite_border_black_20dp
                )
                holder.tvCollectionCount.setDrawable(drawable, 17f, 17f, 2)
                holder.tvDescription.text = item.data.content.data.description
                holder.tvNickName.text = item.data.content.data.owner.nickname
                when (item.data.content.type) {
                    STR_VIDEO_TYPE -> {
                        holder.ivPlay.visible()
                        holder.itemView.setOnClickListener {
                            val items =
                                snapshot().filter { it!!.type == STR_COMMUNITY_COLUMNS_CARD && it.data.dataType == STR_FOLLOW_CARD_DATA_TYPE }
                            UgcDetailActivity.start(
                                fragment.requireActivity(),
                                items.map { it!! },
                                item
                            )
                        }
                    }

                    STR_UGC_PICTURE_TYPE -> {
                        if (!item.data.content.data.urls.isNullOrEmpty() && item.data.content.data.urls.size > 1) holder.ivLayers.visible()
                        holder.itemView.setOnClickListener {
                            val items =
                                snapshot().filter { it!!.type == STR_COMMUNITY_COLUMNS_CARD && it.data.dataType == STR_FOLLOW_CARD_DATA_TYPE }
                            UgcDetailActivity.start(
                                fragment.requireActivity(),
                                items.map { it!! },
                                item
                            )
                        }
                    }
                }
            }

            else -> {
                holder.itemView.gone()
                if (BuildConfig.DEBUG) "${TAG}:${Const.Toast.BIND_VIEWHOLDER_TYPE_WARN}\n${holder}".showToast()
            }
        }
    }

    class HorizontalScrollCardItemCollectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
    }

    class HorizontalScrollCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bannerViewPager =
            view.findViewById<BannerViewPager<CommunityRecommend.ItemX, BannerAdapter.ViewHolder>>(R.id.bannerViewPager)
    }


    class FollowCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivBgPicture: ImageView = view.findViewById(R.id.ivBgPicture)
        val tvChoice: TextView = view.findViewById(R.id.tvChoice)
        val ivLayers: ImageView = view.findViewById(R.id.ivLayers)
        val ivPlay: ImageView = view.findViewById(R.id.ivPlay)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val ivAvatar: ImageView = view.findViewById(R.id.ivAvatar)
        val ivRoundAvatar: CircleImageView = view.findViewById(R.id.ivRoundAvatar)
        val tvNickName: TextView = view.findViewById(R.id.tvNickName)
        val tvCollectionCount: TextView = view.findViewById(R.id.tvCollectionCount)
    }

    /**
     * 根据屏幕比例计算图片高
     *
     * @param originalWidth   服务器图片原始尺寸：宽
     * @param originalHeight  服务器图片原始尺寸：高
     * @return 根据比例缩放后的图片高
     */
    private fun calculateImageHeight(originalWidth: Int, originalHeight: Int): Int {
        //服务器数据异常处理
        val maxImageWidth = DataCalculationUtil.getMaxImageWidth(fragment.requireActivity())
        if (originalWidth == 0 || originalHeight == 0) {
            Log.w(TAG, GlobalUtil.getString(R.string.image_size_error))
            return maxImageWidth
        }
        return maxImageWidth * originalHeight / originalWidth
    }

    companion object {
        const val TAG = "CommendAdapter"
        const val STR_HORIZONTAL_SCROLLCARD_TYPE = "horizontalScrollCard"
        const val STR_COMMUNITY_COLUMNS_CARD = "communityColumnsCard"

        const val STR_HORIZONTAL_SCROLLCARD_DATA_TYPE = "HorizontalScrollCard"
        const val STR_ITEM_COLLECTION_DATA_TYPE = "ItemCollection"
        const val STR_FOLLOW_CARD_DATA_TYPE = "FollowCard"

        const val HORIZONTAL_SCROLLCARD_ITEM_COLLECTION_TYPE =
            1   //type:horizontalScrollCard -> dataType:ItemCollection
        const val HORIZONTAL_SCROLLCARD_TYPE =
            2                   //type:horizontalScrollCard -> dataType:HorizontalScrollCard
        const val FOLLOW_CARD_TYPE =
            3                             //type:communityColumnsCard -> dataType:FollowCard

        const val STR_VIDEO_TYPE = "video"
        const val STR_UGC_PICTURE_TYPE = "ugcPicture"

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CommunityRecommend.Item>() {
            override fun areItemsTheSame(
                oldItem: CommunityRecommend.Item,
                newItem: CommunityRecommend.Item
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CommunityRecommend.Item,
                newItem: CommunityRecommend.Item
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}