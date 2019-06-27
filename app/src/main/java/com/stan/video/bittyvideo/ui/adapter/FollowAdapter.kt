package com.stan.video.bittyvideo.ui.adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.glide.GlideApp
import com.stan.video.bittyvideo.mvp.model.bean.HomeBean
import com.stan.video.bittyvideo.view.MultipleType
import com.stan.video.bittyvideo.view.ViewHolder
import com.stan.video.bittyvideo.view.adapter.CommonAdapter

/**
 * Created by Stan
 * on 2019/6/25.
 */
class FollowAdapter(mContext: Context,lists: ArrayList<HomeBean.Issue.Item>):
        CommonAdapter<HomeBean.Issue.Item>(mContext,lists, object : MultipleType<HomeBean.Issue.Item>{
            override fun getLayoutId(item: HomeBean.Issue.Item, position: Int): Int {
                return when{
                    item.type == "videoCollectionWithBrief" ->
                            R.layout.item_follow
                    else ->
                            throw IllegalAccessException("无此类型")

                }
             }
        })
{
    fun setData(dataList: ArrayList<HomeBean.Issue.Item>){
        mData.addAll(dataList)
        notifyDataSetChanged()
    }
    override fun bindData(holder: ViewHolder, data: HomeBean.Issue.Item, position: Int) {
        when{
            data.type == "videoCollectionWithBrief" ->
                    setAutorInfo(data,holder)
        }
    }

    private fun setAutorInfo(data: HomeBean.Issue.Item, holder: ViewHolder) {
        val headerData = data.data?.header
        holder.setImagePath(R.id.iv_avater,object : ViewHolder.HolderImageLoader(headerData?.icon!!){
            override fun loadImage(iv: ImageView, path: String) {
                GlideApp.with(mContext)
                        .load(path)
                        .placeholder(R.mipmap.default_avatar).circleCrop()
                        .transition(DrawableTransitionOptions().crossFade())
                        .into(iv)
            }

        })
        holder.setText(R.id.tv_title,headerData.title)
        holder.setText(R.id.tv_desc,headerData.description)

        val recyclerView = holder.getView<RecyclerView>(R.id.fl_recyclerView)

        recyclerView.run {
            layoutManager = LinearLayoutManager(mContext as Activity, LinearLayoutManager.HORIZONTAL,false)
            adapter = FollwHorizontalAdapter(mContext,data.data?.itemList,R.layout.item_follow_horizontal)
        }

    }

}