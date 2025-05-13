package com.stan.video.bittyvideo.mvp.model.bean

import kotlinx.android.parcel.RawValue
import java.io.Serializable

/**
 *@Author Stan
 *@Description 视频对应的具体信息，响应实体类。
 *@Date 2025/4/10 17:36
 */


data class Author(
    val adTrack: @RawValue Any?,
    val approvedNotReadyVideoCount: Int,
    val description: String,
    val expert: Boolean,
    val follow: Follow,
    val icon: String?,
    val id: Int,
    val ifPgc: Boolean,
    val latestReleaseTime: Long,
    val link: String,
    val name: String,
    val recSort: Int,
    val shield: Shield,
    val videoNum: Int
) : Serializable {


    data class Follow(val followed: Boolean, val itemId: Int, val itemType: String) : Serializable

    data class Shield(val itemId: Int, val itemType: String, val shielded: Boolean) : Serializable
}

data class Provider(val alias: String, val icon: String, val name: String)

data class Tag(
    val actionUrl: String,
    val adTrack: Any,
    val bgPicture: String,
    val childTagIdList: Any,
    val childTagList: Any,
    val communityIndex: Int,
    val desc: String,
    val haveReward: Boolean,
    val headerImage: String,
    val id: Int,
    val ifNewest: Boolean,
    val name: String,
    val newestEndTime: Any,
    val tagRecType: String
)


data class Cover(
    val blurred: String,
    val detail: String,
    val feed: String,
    val homepage: String?,
    val sharing: String?
) :
    Serializable


data class Consumption(
    val collectionCount: Int,
    val realCollectionCount: Int,
    val replyCount: Int,
    val shareCount: Int
) : Serializable