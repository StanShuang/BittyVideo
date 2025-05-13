package com.stan.video.bittyvideo.mvvm.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stan.video.bittyvideo.mvvm.dao.MainPageRepository
import com.stan.video.bittyvideo.mvvm.viewmodel.SearchViewModel

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(private val repository: MainPageRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(repository) as T
    }

}
