package com.stan.video.bittyvideo.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.view.View
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
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
 * on 2019/6/26.
 */
class CategoryDetailAdapter(context: Context,itemList: ArrayList<HomeBean.Issue.Item>,layoutId: Int): CommonAdapter<HomeBean.Issue.Item>(context,itemList,layoutId) {
    fun addData(items: ArrayList<HomeBean.Issue.Item>){
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun bindData(holder: ViewHolder, data: HomeBean.Issue.Item, position: Int) {
        setVidoItem(holder,data)
    }

    private fun setVidoItem(holder: ViewHolder, data: HomeBean.Issue.Item) {
        val itemData = data.data
        val cover = itemData?.cover?.feed ?: ""
        GlideApp.with(mContext)
                .load(cover)
                .apply(RequestOptions().placeholder(R.drawable.placeholder_banner))
                .transition(DrawableTransitionOptions().crossFade())
                .into(holder.getView(R.id.iv_image))
        holder.setText(R.id.tv_title,itemData?.title ?: "")
        val timeFormat = durationFormat(itemData?.duration)
        holder.setText(R.id.tv_tag,"#${itemData?.category} / $timeFormat")
        holder.setOnItemClickListener(listener = View.OnClickListener {
            gotoVideoPlayer(mContext as Activity,holder.getView(R.id.iv_image),data)
        })
    }

    private fun gotoVideoPlayer(activity: Activity, view: View, data: HomeBean.Issue.Item) {
        Intent(activity,VideoDetailActivity :: class.java).run {
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