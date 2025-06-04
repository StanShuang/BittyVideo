package com.stan.video.bittyvideo.mvvm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stan.video.bittyvideo.R

/**
 *@Author Stan
 *@Description PagingDataAdapter添加Footer
 *@Date 2025/5/22 14:57
 */
class FooterAdapter(val retry: () -> Unit) : LoadStateAdapter<FooterAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        val retryButton: TextView = itemView.findViewById(R.id.retryButton)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        loadState: LoadState
    ) {
        holder.progressBar.isVisible = loadState is LoadState.Loading
        holder.retryButton.isVisible = loadState is LoadState.Error
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_footer_item, parent, false)
        return ViewHolder(view).apply {
            retryButton.setOnClickListener {
                retry
            }
        }
    }
}