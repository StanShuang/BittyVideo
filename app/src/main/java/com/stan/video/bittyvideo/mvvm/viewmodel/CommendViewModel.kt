package com.stan.video.bittyvideo.mvvm.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.stan.video.bittyvideo.mvp.model.bean.CommunityRecommend
import com.stan.video.bittyvideo.mvvm.dao.MainPageRepository
import kotlinx.coroutines.flow.Flow

/**
 *@Author Stan
 *@Description
 *@Date 2025/4/11 10:40
 */
class CommendViewModel(private val repository: MainPageRepository) : ViewModel() {

    var dataList = ArrayList<CommunityRecommend.Item>()

    fun getPagingData(): Flow<PagingData<CommunityRecommend.Item>> {
        return repository.getCommunityRecommendPagingData().cachedIn(viewModelScope)
    }
}