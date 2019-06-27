package com.stan.video.bittyvideo.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import com.stan.video.bittyvideo.view.MultipleType
import com.stan.video.bittyvideo.view.ViewHolder

/**
 * Created by Stan
 * on 2019/6/11.
 * 通用adapter
 */
abstract class CommonAdapter<T>(var mContext: Context, var mData: ArrayList<T>,//条目布局
                                    private var mLayoutId: Int): RecyclerView.Adapter<ViewHolder>() {
    protected var mInflater: LayoutInflater? = null
    protected var mTypeSupport: MultipleType<T>?= null

    init {
        mInflater = LayoutInflater.from(mContext)
    }
    //使用接口回调点击事件
    private var mItemClickListener: OnItemClickListener? = null

    //使用接口回调点击事件
    private var mItemLongClickListener: OnItemLongClickListener? = null
    //需要多布局
    constructor(context: Context,data: ArrayList<T>,typeSuppot: MultipleType<T>): this(context,data,-1){
        this.mTypeSupport = typeSuppot
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(mTypeSupport != null){
            //需要多布局
            mLayoutId = viewType
        }
        //创建view
        val view = mInflater?.inflate(mLayoutId,parent,false)
        return ViewHolder(view!!)
    }

    override fun getItemViewType(position: Int): Int {
        //多布局问题
        return mTypeSupport?.getLayoutId(mData[position],position) ?: super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //绑定数据
        bindData(holder,mData[position],position)
        //条目点击事件
        mItemClickListener?.let {
            holder.itemView.setOnClickListener { mItemClickListener!!.onItemClick(mData[position],position) }
        }
        //长按点击事件
        mItemLongClickListener?.let {
            holder.itemView.setOnLongClickListener { mItemLongClickListener!!.onItemLongClick(mData[position], position) }
        }

    }

    /**
     * 将必要参数传递出去
     */
    protected abstract fun bindData(holder: ViewHolder, data: T, position: Int)
    override fun getItemCount(): Int {
        return mData.size
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.mItemClickListener = itemClickListener
    }

    fun setOnItemLongClickListener(itemLongClickListener: OnItemLongClickListener) {
        this.mItemLongClickListener = itemLongClickListener
    }

}