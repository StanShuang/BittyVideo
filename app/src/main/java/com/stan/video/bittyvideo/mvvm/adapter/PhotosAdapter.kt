package com.stan.video.bittyvideo.mvvm.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.chrisbanes.photoview.PhotoView
import com.stan.video.bittyvideo.view.load

/**
 *@Author Stan
 *@Description
 *@Date 2025/6/12 17:22
 */
class PhotosAdapter(
    private val dataList: List<String>,
    private val ugcHolder: UgcDetailAdapter.FollowCardViewHolder
) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val photoView = PhotoView(parent.context)
        photoView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return ViewHolder(photoView)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.photoView.load(dataList[position])
        holder.photoView.setOnClickListener {
            ugcHolder.switchHeaderAndUgcInfoVisibility()
        }
    }

    override fun getItemCount(): Int = dataList.size
    class ViewHolder(view: PhotoView) : RecyclerView.ViewHolder(view) {
        val photoView = view
    }
}
