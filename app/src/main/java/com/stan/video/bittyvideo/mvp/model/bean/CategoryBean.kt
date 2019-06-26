package com.stan.video.bittyvideo.mvp.model.bean

import java.io.Serializable

/**
 * Created by Stan
 * on 2019/6/26.
 */
data class CategoryBean(val id: Long, val name: String, val description: String, val bgPicture: String, val bgColor: String, val headerImage: String) : Serializable