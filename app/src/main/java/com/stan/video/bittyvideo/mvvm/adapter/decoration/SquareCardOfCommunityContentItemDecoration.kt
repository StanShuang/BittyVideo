package com.stan.video.bittyvideo.mvvm.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.stan.video.bittyvideo.view.dp2px

/**
 *@Author Stan
 *@Description
 *@Date 2025/6/5 15:46
 */
class SquareCardOfCommunityContentItemDecoration() : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        // item position
        val position = parent.getChildAdapterPosition(view)
        //item count
        val count = parent.adapter?.itemCount?.minus(1)

        /**
         * 列表中间内间距，左or右。
         */
        val middleSpace = dp2px(3f)
        when (position) {
            0 -> {
                /*outRect.left = fragment.bothSideSpace*/
                outRect.right = middleSpace
            }

            count -> {
                outRect.left = middleSpace
                /*outRect.right = fragment.bothSideSpace*/
            }

            else -> {
                outRect.left = middleSpace
                outRect.right = middleSpace
            }
        }
    }
}