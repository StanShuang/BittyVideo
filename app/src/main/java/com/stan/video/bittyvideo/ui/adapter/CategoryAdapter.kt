package com.stan.video.bittyvideo.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.app.MyApplication
import com.stan.video.bittyvideo.glide.GlideApp
import com.stan.video.bittyvideo.mvp.model.bean.CategoryBean
import com.stan.video.bittyvideo.recycleview.ViewHolder
import com.stan.video.bittyvideo.recycleview.adapter.CommonAdapter
import com.stan.video.bittyvideo.ui.activity.CategoryDetailActivity
import com.stan.video.bittyvideo.utils.Constant

/**
 * Created by Stan
 * on 2019/6/26.
 */
class CategoryAdapter(mContext: Context,categoryList: ArrayList<CategoryBean>,layoutId: Int): CommonAdapter<CategoryBean>(mContext,categoryList,layoutId) {
    private var typeFace: Typeface? = null
    init {
        typeFace = Typeface.createFromAsset(MyApplication.context.assets,"fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")
    }
    fun setCategoryData(item: ArrayList<CategoryBean>){
        mData.clear()
        mData = item
        notifyDataSetChanged()
    }
    override fun bindData(holder: ViewHolder, data: CategoryBean, position: Int) {
        holder.setText(R.id.tv_category_name,"#${data.name}")
        holder.getView<TextView>(R.id.tv_category_name).typeface = typeFace
        holder.setImagePath(R.id.iv_category,object : ViewHolder.HolderImageLoader(data.bgPicture){
            override fun loadImage(iv: ImageView, path: String) {
                GlideApp.with(mContext)
                        .load(path)
                        .placeholder(R.color.color_darker_gray)
                        .transition(DrawableTransitionOptions().crossFade())
                        .thumbnail(0.5f)
                        .into(iv)
            }

        })
        holder.setOnItemClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                Intent(mContext as Activity, CategoryDetailActivity::class.java).run{
                    putExtra(Constant.BUNDLE_CATEGORY_DATA,data)
                    mContext.startActivity(this)
                }
            }

        })

    }
}