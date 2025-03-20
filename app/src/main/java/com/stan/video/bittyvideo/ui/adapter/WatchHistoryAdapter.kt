package com.stan.video.bittyvideo.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.util.Pair
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.ext.durationFormat
import com.stan.video.bittyvideo.glide.GlideApp
import com.stan.video.bittyvideo.mvp.model.bean.NewWatchHistoryBean
import com.stan.video.bittyvideo.mvp.model.bean.WatchHistoryBean
import com.stan.video.bittyvideo.ui.activity.VideoDetailActivity
import com.stan.video.bittyvideo.utils.Constant
import com.stan.video.bittyvideo.view.ViewHolder
import com.stan.video.bittyvideo.view.adapter.CommonAdapter

/**
 * Created by Stan
 * on 2019/7/4.
 */
class WatchHistoryAdapter(context: Context, itemLists: ArrayList<NewWatchHistoryBean>, layoutId: Int): CommonAdapter<NewWatchHistoryBean>(context,itemLists,layoutId) {

    fun addData(items: ArrayList<NewWatchHistoryBean>){
        mData.clear()
        mData = items
        notifyDataSetChanged()
    }
    override fun bindData(holder: ViewHolder, data: NewWatchHistoryBean, position: Int) {
        with(holder) {
           val item = data
            setText(R.id.tv_title_card, item?.title!!)
            setText(R.id.tv_tag_card, "#${item.category} / ${durationFormat(item.duration)}")
            setImagePath(R.id.iv_video_small_card, object : ViewHolder.HolderImageLoader(item.detail) {
                override fun loadImage(iv: ImageView, path: String) {
                    GlideApp.with(mContext)
                            .load(path)
                            .placeholder(R.drawable.placeholder_banner)
                            .transition(DrawableTransitionOptions().crossFade())
                            .into(iv)
                }
            })
        }
        holder.getView<TextView>(R.id.tv_title_card).setTextColor(ContextCompat.getColor(mContext,R.color.color_black))
        holder.setOnItemClickListener(listener = View.OnClickListener {
//            goToVideoPlayer(mContext as Activity, holder.getView(R.id.iv_video_small_card), data)
        })
    }
    /**
     * 跳转到视频详情页面播放
     *
     * @param activity
     * @param view
     */
    private fun goToVideoPlayer(activity: Activity, view: View, itemData: WatchHistoryBean) {
        Intent(activity,VideoDetailActivity :: class.java).run{
            putExtra(Constant.BUNDLE_VIDEO_DATA,itemData.data)
            putExtra(VideoDetailActivity.Companion.TRANSITION,true)
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                val pair = Pair<View,String>(view,VideoDetailActivity.IMG_TRANSITION)
                val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,pair)
                ActivityCompat.startActivity(activity,this,activityOptions.toBundle())
            }else{
                activity.startActivity(this)
                activity.overridePendingTransition(R.anim.anim_in,R.anim.anim_out)
            }
        }
    }
}