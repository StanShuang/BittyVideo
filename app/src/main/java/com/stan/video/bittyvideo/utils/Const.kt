package com.stan.video.bittyvideo.utils

/**
 *@Author Stan
 *@Description
 *@Date 2025/4/10 14:41
 */
interface Const {
    interface ItemViewType {
        companion object {
            const val UNKNOWN = -1              //未知类型，使用EmptyViewHolder容错处理。
            const val CUSTOM_HEADER = 0         //自定义头部类型。
            const val MAX = 100   //避免外部其他类型与此处包含的某个类型重复。
        }

    }

    interface Toast {
        companion object {
            const val BIND_VIEWHOLDER_TYPE_WARN = "bindViewHolder Type Unprocessed"
        }

    }

    interface Config {
        companion object {
            const val PAGE_SIZE = 50
        }
    }
}