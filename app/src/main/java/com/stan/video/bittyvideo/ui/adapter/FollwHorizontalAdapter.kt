package com.stan.video.bittyvideo.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.ext.durationFormat
import com.stan.video.bittyvideo.glide.GlideApp
import com.stan.video.bittyvideo.mvp.model.bean.HomeBean
import com.stan.video.bittyvideo.view.ViewHolder
import com.stan.video.bittyvideo.view.adapter.CommonAdapter
import com.stan.video.bittyvideo.ui.activity.VideoDetailActivity
import com.stan.video.bittyvideo.utils.Constant

/**
 * Created by Stan
 * on 2019/6/25.
 */
class FollwHorizontalAdapter(mContext: Context,items: ArrayList<HomeBean.Issue.Item>,layoutId: Int): CommonAdapter<HomeBean.Issue.Item>(mContext,items,layoutId) {
    override fun bindData(holder: ViewHolder, data: HomeBean.Issue.Item, position: Int) {
        val itemData = data.data
        holder.setImagePath(R.id.iv_cover_feed,object : ViewHolder.HolderImageLoader(itemData?.cover?.feed!!){
            override fun loadImage(iv: ImageView, path: String) {
                GlideApp.with(mContext)
                        .load(path)
                        .placeholder(R.drawable.placeholder_banner)
                        .transition(DrawableTransitionOptions().crossFade())
                        .into(holder.getView(R.id.iv_cover_feed))
            }

        })
        holder.setText(R.id.tv_title,itemData.title ?: "")
        //格式化时间
        val timeFormat = durationFormat(itemData.duration)
        with(holder) {
            if (itemData?.tags != null && itemData.tags.size > 0) {
                setText(R.id.tv_tag,"#${itemData.tags[0].name}  / $timeFormat")
            }else{
                setText(R.id.tv_tag,"#$timeFormat")
            }
            setOnItemClickListener(listener = View.OnClickListener {
                gotoVideoPlayer(mContext as Activity,holder.getView<ImageView>(R.id.iv_cover_feed),data)
            })

        }
    }

    private fun gotoVideoPlayer(activity: Activity, view: ImageView, data: HomeBean.Issue.Item) {
        Intent(activity,VideoDetailActivity :: class.java).run{
            putExtra(Constant.BUNDLE_VIDEO_DATA,data)
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