package com.stan.video.bittyvideo.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.stan.video.bittyvideo.mvp.model.bean.CommunityRecommend

/**
 *@Author Stan
 *@Description
 *@Date 2025/6/10 17:04
 */
class UgcDetailViewModel : ViewModel() {
    var dataList: List<CommunityRecommend.Item>? = null

    var itemPosition: Int = -1
}