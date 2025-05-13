package com.stan.video.bittyvideo.mvvm.dao

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.stan.video.bittyvideo.mvp.model.bean.CommunityRecommend
import com.stan.video.bittyvideo.mvp.model.bean.Follow
import com.stan.video.bittyvideo.mvvm.net.CommendPagingSource
import com.stan.video.bittyvideo.mvvm.net.FollowPagingSource
import com.stan.video.bittyvideo.mvvm.net.MainPageDao
import com.stan.video.bittyvideo.mvvm.net.NetworkRequestManager
import com.stan.video.bittyvideo.utils.Const
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 *@Author Stan
 *@Description
 *@Date 2025/4/10 10:14
 */
class MainPageRepository private constructor(
    private val networkRequestManager: NetworkRequestManager,
    private val mainPageDao: MainPageDao
) {

    suspend fun refreshHotSearch() = requestHotSearch()

    private suspend fun requestHotSearch() = withContext(Dispatchers.IO) {
        val response = networkRequestManager.fetchHotSearch()
        mainPageDao.cacheHotSearch(response)
        response
    }

    fun getCommunityRecommendPagingData(): Flow<PagingData<CommunityRecommend.Item>> {
        return Pager(
            config = PagingConfig(Const.Config.PAGE_SIZE),
            pagingSourceFactory = { CommendPagingSource(networkRequestManager.mainPageService) }
        ).flow
    }

    fun getFollowPagingData(): Flow<PagingData<Follow.Item>> {
        return Pager(
            config = PagingConfig(Const.Config.PAGE_SIZE),
            pagingSourceFactory = { FollowPagingSource(networkRequestManager.mainPageService) }
        ).flow
    }

    companion object {
        @Volatile
        private var INSTANCE: MainPageRepository? = null

        fun getInstance(dao: MainPageDao, network: NetworkRequestManager): MainPageRepository {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = MainPageRepository(network, dao)
                    }
                }
            }
            return INSTANCE!!
        }
    }
}