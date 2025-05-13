package com.stan.video.bittyvideo.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.stan.video.bittyvideo.mvvm.dao.MainPageRepository
import com.stan.video.bittyvideo.net.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class SearchViewModel(private val repository: MainPageRepository) : ViewModel() {

    val dataList = ArrayList<String>()

    private var requestParamLiveData = MutableLiveData<Any>()

    val dataListLiveData = Transformations.switchMap(requestParamLiveData) {
        liveData {
            val result = try {
                val hotSearch = repository.refreshHotSearch()
                Result.success(hotSearch)
            } catch (e: Exception) {
                Result.failure<List<String>>(e)
            }
            emit(result)
        }

    }

    fun onRefresh() {
        requestParamLiveData.value = requestParamLiveData.value
    }


}
