package com.stan.video.bittyvideo.ui.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.flexbox.FlexboxLayoutManager
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.view.ViewHolder
import com.stan.video.bittyvideo.view.adapter.CommonAdapter

/**
 * Created by Stan
 * on 2019/7/1.
 */
class HotKeywordsAdapter(context: Context,mList: ArrayList<String>,layoutId: Int): CommonAdapter<String>(context,mList,layoutId) {
    override fun bindData(holder: ViewHolder, data: String, position: Int) {
        holder.setText(R.id.tv_title,data)
        val params = holder.getView<TextView>(R.id.tv_title)
        if(params is FlexboxLayoutManager.LayoutParams){
            params.flexGrow = 1.0f
        }
        holder.setOnItemClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                onTagItemClick?.invoke(data)
            }

        })

    }
    private var onTagItemClick: ((tag: String) -> Unit)? = null
    fun setOnTagItemClickListener(onTagItemClickListener: (tag: String) -> Unit){
        this.onTagItemClick = onTagItemClickListener
    }
}