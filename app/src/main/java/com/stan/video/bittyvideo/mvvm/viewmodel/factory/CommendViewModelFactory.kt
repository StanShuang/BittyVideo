package com.stan.video.bittyvideo.mvvm.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stan.video.bittyvideo.mvvm.dao.MainPageRepository
import com.stan.video.bittyvideo.mvvm.viewmodel.CommendViewModel

/**
 *@Author Stan
 *@Description
 *@Date 2025/4/11 10:43
 */
@Suppress("UNCHECKED_CAST")
class CommendViewModelFactory(private val repository: MainPageRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommendViewModel(repository) as T
    }
}