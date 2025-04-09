package com.stan.video.bittyvideo.ui.adapter


import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import android.view.View
import android.view.ViewGroup
import cn.bingoogolapple.bgabanner.BGABanner
import com.bumptech.glide.Glide

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.ext.durationFormat
import com.stan.video.bittyvideo.mvp.model.bean.HomeBean
import com.stan.video.bittyvideo.ui.activity.VideoDetailActivity
import com.stan.video.bittyvideo.utils.Constant
import io.reactivex.Observable
import com.stan.video.bittyvideo.view.ViewHolder
import com.stan.video.bittyvideo.view.adapter.CommonAdapter

/**
 * Created by Stan
 * on 2019/6/13.
 */
class HomeAdapter(private val context: Context,data:ArrayList<HomeBean.Issue.Item>): CommonAdapter<HomeBean.Issue.Item>(context,data,-1) {
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

    /**
     * 创建布局
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType){
            ITEM_TYPE_BANNER ->
                    ViewHolder(infaterView(R.layout.item_home_banner, parent))
            ITEM_TYPE_TEXT_HEADER ->
                    ViewHolder(infaterView(R.layout.item_home_header,parent))
            else ->
                    ViewHolder(infaterView(R.layout.item_home_content,parent))
        }
    }

    private fun infaterView(mLayoutId: Int, parent: ViewGroup): View {
        val view = mInflater?.inflate(mLayoutId,parent,false)
        return view ?: View(parent.context)
    }

    /**
     * 绑定布局
     */
    override fun bindData(holder: ViewHolder, data: HomeBean.Issue.Item, position: Int) {
        when(getItemViewType(position)){
            ITEM_TYPE_BANNER -> {
                val bannerItemData: ArrayList<HomeBean.Issue.Item> = mData.take(bannerItemSize).toCollection(ArrayList())
                val bannerFeedList = ArrayList<String>()
                val bannerTitleList = ArrayList<String>()
                //取出banner 显示的 img 和 Title
                Observable.fromIterable(bannerItemData)
                        .subscribe { list ->
                            bannerFeedList.add(list.data?.cover?.feed ?: "")
                            bannerTitleList.add(list.data?.title ?: "")
                        }
                //设置banner
                with(holder){
                    getView<BGABanner>(R.id.banner).run {
                        setAutoPlayAble(bannerFeedList.size > 1)
                        setData(bannerFeedList,bannerTitleList)
                        setAdapter { banner, _, model, position ->
                            Glide.with(mContext)
                                    .load(model)
                                    .placeholder(R.drawable.placeholder_banner)
                                    .transition(DrawableTransitionOptions().crossFade())
                                    .into(banner.getItemImageView(position))
                        }
                    }
                }
                holder.getView<BGABanner>(R.id.banner).setDelegate { _, itemView, _, position ->

                    goToVideoPlayer(mContext as Activity,itemView,bannerItemData[position])
                }

            }
            ITEM_TYPE_TEXT_HEADER -> {
                holder.setText(R.id.tvHeader,mData[position + bannerItemSize -1].data?.text ?: "")
            }
            ITEM_TYPE_CONTENT -> {
                setVideoItme(holder,mData[position + bannerItemSize -1])
            }

        }
    }

    /**
     * 加载 content item
     * @param item 数据
     */
    private fun setVideoItme(holder: ViewHolder, item: HomeBean.Issue.Item) {
        val itemData = item.data
        val defAvatar = R.mipmap.default_avatar
        val cover = itemData?.cover?.feed
        var avatar = itemData?.author?.icon
        var tagText: String? = "#"
        // 作者出处为空，就显获取提供者的信息
        if(avatar.isNullOrEmpty()){
            avatar = itemData?.provider?.icon
        }
        // 加载封页图
        Glide.with(mContext)
                .load(cover)
                .placeholder(R.drawable.placeholder_banner)
                .transition(DrawableTransitionOptions().crossFade())
                .into(holder.getView(R.id.iv_cover_feed))
        // 如果提供者信息为空，就显示默认
        if (avatar.isNullOrEmpty()) {
            Glide.with(mContext)
                    .load(defAvatar)
                    .placeholder(R.mipmap.default_avatar).circleCrop()
                    .transition(DrawableTransitionOptions().crossFade())
                    .into(holder.getView(R.id.iv_avatar))

        } else {
            Glide.with(mContext)
                    .load(avatar)
                    .placeholder(R.mipmap.default_avatar).circleCrop()
                    .transition(DrawableTransitionOptions().crossFade())
                    .into(holder.getView(R.id.iv_avatar))
        }
        holder.setText(R.id.tv_title, itemData?.title ?: "")
        //遍历标签
        itemData?.tags?.take(4)?.forEach {
            tagText += (it.name + "/")
        }
        // 格式化时间
        val timeFormat = durationFormat(itemData?.duration)

        tagText += timeFormat

        holder.setText(R.id.tv_tag, tagText!!)

        holder.setText(R.id.tv_category, "#" + itemData?.category)

        holder.setOnItemClickListener(listener = View.OnClickListener {

            goToVideoPlayer(mContext as Activity, holder.getView(R.id.iv_cover_feed), item)
        })
    }
    /**
     * 跳转到视频详情页面播放
     *
     * @param activity
     * @param view
     */
    private fun goToVideoPlayer(activity: Activity,view: View,itemData: HomeBean.Issue.Item){
        Intent(activity,VideoDetailActivity :: class.java).run {
            putExtra(Constant.BUNDLE_VIDEO_DATA,itemData)
            putExtra(VideoDetailActivity.TRANSITION,true)
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                val pair = Pair(view,VideoDetailActivity.IMG_TRANSITION)
                val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,pair)
                ActivityCompat.startActivity(activity,this,activityOptions.toBundle())
            }else{
                activity.startActivity(this)
                activity.overridePendingTransition(R.anim.anim_in,R.anim.anim_out)
            }

        }

    }
}