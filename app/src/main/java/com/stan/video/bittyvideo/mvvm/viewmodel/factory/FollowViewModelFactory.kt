package com.stan.video.bittyvideo.mvvm.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stan.video.bittyvideo.mvvm.dao.MainPageRepository
import com.stan.video.bittyvideo.mvvm.viewmodel.FollowViewModel

/**
 *@Author Stan
 *@Description
 *@Date 2025/4/11 11:17
 */
@Suppress("UNCHECKED_CAST")
class FollowViewModelFactory(private val repository: MainPageRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FollowViewModel(repository) as T
    }
}