package com.stan.video.bittyvideo.mvvm.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import com.stan.video.bittyvideo.BuildConfig
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.mvp.model.bean.Follow
import com.stan.video.bittyvideo.mvvm.fragment.FollowFragment
import com.stan.video.bittyvideo.utils.Const
import com.stan.video.bittyvideo.utils.DateUtil
import com.stan.video.bittyvideo.utils.GlobalUtil.setOnClickListener
import com.stan.video.bittyvideo.utils.showToast
import com.stan.video.bittyvideo.view.EmptyViewHolder
import com.stan.video.bittyvideo.view.VideoListener
import com.stan.video.bittyvideo.view.conversionVideoDuration
import com.stan.video.bittyvideo.view.gone
import com.stan.video.bittyvideo.view.inflate
import com.stan.video.bittyvideo.view.load
import com.stan.video.bittyvideo.view.visible

/**
 *@Author Stan
 *@Description
 *@Date 2025/5/21 14:56
 */
class FollowAdapter(val fragment: FollowFragment) :
    PagingDataAdapter<Follow.Item, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int = when {
        position == 0 -> Const.ItemViewType.CUSTOM_HEADER
        getItem(position - 1)!!.type == "autoPlayFollowCard" && getItem(position - 1)!!.data.dataType == "FollowCard" -> AUTO_PLAY_FOLLOW_CARD
        else -> Const.ItemViewType.UNKNOWN
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        Const.ItemViewType.CUSTOM_HEADER -> HeaderViewHolder(
            R.layout.item_community_follow_header_type.inflate(
                parent
            )
        )

        Const.ItemViewType.MAX -> AutoPlayFollowCardViewHolder(
            R.layout.item_community_auto_play_follow_card_follow_card_type.inflate(
                parent
            )
        )

        else -> EmptyViewHolder(View(parent.context))
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (holder) {
            is HeaderViewHolder -> holder.itemView.setOnClickListener { }
            is AutoPlayFollowCardViewHolder -> {
                val item = getItem(position - 1)!!
                item.data.content.data.run {
                    holder.ivAvater.load(item.data.header.icon ?: author?.icon ?: "")
                    holder.tvReleaseTime.text = DateUtil.getDate(
                        releaseTime ?: author?.latestReleaseTime ?: System.currentTimeMillis(),
                        "HH:mm"
                    )
                    holder.tvTitle.text = title
                    holder.tvNickname.text = author?.name ?: ""
                    holder.tvContent.text = description
                    holder.tvCollectionCount.text = consumption.collectionCount.toString()
                    holder.tvReplyCount.text = consumption.replyCount.toString()
                    holder.tvVideoDuration.visible()    //视频播放后，复用tvVideoDuration直接隐藏了
                    holder.tvVideoDuration.text = duration.conversionVideoDuration()
                    CommendAdapter.startAutoPlay(
                        fragment.requireActivity(),
                        holder.videoPlayer,
                        position,
                        playUrl,
                        cover.feed,
                        TAG,
                        object :
                            VideoListener {
                            override fun onPrepared(url: String, vararg objects: Any) {
                                super.onPrepared(url, *objects)
                                holder.tvVideoDuration.gone()
                                GSYVideoManager.instance().isNeedMute = true
                            }

                            override fun onClickResume(url: String, vararg objects: Any) {
                                super.onClickResume(url, *objects)
                                holder.tvVideoDuration.gone()
                            }

                            override fun onClickBlank(url: String, vararg objects: Any) {
                                super.onClickBlank(url, *objects)
                                holder.tvVideoDuration.visible()
                            }
                        }


                    )
                    holder.let {
                        setOnClickListener(
                            it.videoPlayer.thumbImageView,
                            it.itemView,
                            it.ivCollectionCount,
                            it.tvCollectionCount,
                            it.ivFavorites,
                            it.tvFavorites,
                            it.ivShare
                        ) {
                            when (this) {
                                it.videoPlayer.thumbImageView, it.itemView -> {

                                }

                                it.ivCollectionCount, it.tvCollectionCount, it.ivFavorites, it.tvFavorites -> {
//                                    LoginActivity.start(fragment.activity)
                                }

                                it.ivShare -> {
//                                    showDialogShare(fragment.activity, getShareContent(item))
                                }
                            }
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

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class AutoPlayFollowCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvater = view.findViewById<ImageView>(R.id.ivAvatar)
        val tvReleaseTime = view.findViewById<TextView>(R.id.tvReleaseTime)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvNickname = view.findViewById<TextView>(R.id.tvNickname)
        val tvContent = view.findViewById<TextView>(R.id.tvContent)
        val ivCollectionCount = view.findViewById<ImageView>(R.id.ivCollectionCount)
        val tvCollectionCount = view.findViewById<TextView>(R.id.tvCollectionCount)
        val ivReply = view.findViewById<ImageView>(R.id.ivReply)
        val tvReplyCount = view.findViewById<TextView>(R.id.tvReplyCount)
        val ivFavorites = view.findViewById<ImageView>(R.id.ivFavorites)
        val tvFavorites = view.findViewById<TextView>(R.id.tvFavorites)
        val tvVideoDuration = view.findViewById<TextView>(R.id.tvVideoDuration)
        val ivShare = view.findViewById<ImageView>(R.id.ivShare)
        val videoPlayer: GSYVideoPlayer = view.findViewById(R.id.videoPlayer)

    }

    companion object {
        const val TAG = "FollowAdapter"

        const val AUTO_PLAY_FOLLOW_CARD = Const.ItemViewType.MAX

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Follow.Item>() {
            override fun areItemsTheSame(
                oldItem: Follow.Item,
                newItem: Follow.Item
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Follow.Item,
                newItem: Follow.Item
            ): Boolean {
                return oldItem == newItem
            }
        }

    }

}