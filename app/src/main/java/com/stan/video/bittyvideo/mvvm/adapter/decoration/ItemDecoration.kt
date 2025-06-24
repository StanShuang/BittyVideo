package com.stan.video.bittyvideo.mvvm.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.utils.GlobalUtil
import com.stan.video.bittyvideo.view.dp2px

/**
 *@Author Stan
 *@Description
 *@Date 2025/6/4 15:51
 */
class ItemDecoration() : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex

        /**
         * 列表左or右间距
         */
        val bothSideSpace = GlobalUtil.getDimension(R.dimen.listSpaceSize)

        /**
         * 列表中间内间距，左or右。
         */
        val middleSpace = dp2px(3f)
        outRect.top = bothSideSpace
        when (spanIndex) {
            0 -> {
                outRect.left = bothSideSpace
                outRect.right = middleSpace
            }

            else -> {
                outRect.left = middleSpace
                outRect.right = bothSideSpace
            }
        }
    }
}