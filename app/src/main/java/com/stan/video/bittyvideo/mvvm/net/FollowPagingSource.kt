package com.stan.video.bittyvideo.mvvm.net

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.stan.video.bittyvideo.api.ApiService
import com.stan.video.bittyvideo.mvp.model.bean.Follow
import com.stan.video.bittyvideo.mvvm.dao.MainPageRepository

/**
 *@Author Stan
 *@Description
 *@Date 2025/4/11 11:02
 */
class FollowPagingSource(private val apiService: ApiService) : PagingSource<String, Follow.Item>() {
    override fun getRefreshKey(state: PagingState<String, Follow.Item>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Follow.Item> {
        return try {
            val page = params.key ?: ApiService.FOLLOW_URL
            val repoResponse = apiService.getFollow(page)
            val repoItems = repoResponse.itemList
            val prevKey = null
            val newKey =
                if (repoItems.isNotEmpty() && !repoResponse.nextPageUrl.isNullOrEmpty()) repoResponse.nextPageUrl else null
            Log.d("FollowPagingSource", "nextPageUrl = $newKey")
            LoadResult.Page(repoItems, prevKey, newKey)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }

    }
}