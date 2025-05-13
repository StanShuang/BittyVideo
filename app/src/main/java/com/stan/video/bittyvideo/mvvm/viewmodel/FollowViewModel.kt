package com.stan.video.bittyvideo.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.stan.video.bittyvideo.mvp.model.bean.Follow
import com.stan.video.bittyvideo.mvvm.dao.MainPageRepository
import kotlinx.coroutines.flow.Flow

/**
 *@Author Stan
 *@Description
 *@Date 2025/4/11 11:13
 */
class FollowViewModel(private val repository: MainPageRepository) : ViewModel() {
    var dataList = ArrayList<Follow.Item>()
    fun getPagingData(): Flow<PagingData<Follow.Item>> {
        return repository.getFollowPagingData().cachedIn(viewModelScope)
    }
}