package com.stan.video.bittyvideo.view

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by Stan
 * on 2019/6/13.
 */
@Suppress("UNCHECKED_CAST")
class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    //用于缓存已找的界面
    private var mView: SparseArray<View>? = null
    init {
        mView = SparseArray()
    }
    fun <T: View> getView(viewId: Int): T{
        var view: View? = mView?.get(viewId)
        //使用缓存的方式减少findViewById的次数
        if(view == null){
            view = itemView.findViewById(viewId)
            mView?.put(viewId,view)
        }
        return view as T
    }
    fun <T : ViewGroup> getViewGroup(viewId: Int): T {
        //对已有的view做缓存
        var view: View? = mView?.get(viewId)
        //使用缓存的方式减少findViewById的次数
        if (view == null) {
            view = itemView.findViewById(viewId)
            mView?.put(viewId, view)
        }
        return view as T
    }
    @SuppressLint("SetTextI18n")
    fun setText(viewId: Int,text: CharSequence): ViewHolder{
        val view = getView<TextView>(viewId)
        view.text = text
        //希望可以链式调用
        return this
    }
    fun setHint(viewId: Int,text: CharSequence): ViewHolder{
        val view = getView<TextView>(viewId)
        view.hint = text
        return this
    }

    /**
     * 设置本地图片
     */
    fun setImageResource(viewId: Int,resId: Int):ViewHolder{
        val view = getView<ImageView>(viewId)
        view.setImageResource(resId)
        return this
    }
    /**
     * 加载图片资源
     */
    fun setImagePath(viewId: Int,imageLoader: HolderImageLoader): ViewHolder{
        val view = getView<ImageView>(viewId)
        imageLoader.loadImage(view,imageLoader.path)
        return this
    }
    abstract class HolderImageLoader(val path: String){
        /**
         * 需要去复写这个方法加载图片
         *
         * @param iv
         * @param path
         */
        abstract fun loadImage(iv: ImageView,path: String)
    }
    fun setViewVisibility(viewId: Int,visibility: Int): ViewHolder{
        getView<View>(viewId).visibility = visibility
        return this
    }
    fun setOnItemClickListener(listener: View.OnClickListener){
        itemView.setOnClickListener(listener)
    }
    fun setOnItemLongClickListener(listener: View.OnLongClickListener){
        itemView.setOnLongClickListener(listener)
    }



}