package com.stan.video.bittyvideo.mvvm.net

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.stan.video.bittyvideo.api.ApiService
import com.stan.video.bittyvideo.mvp.model.bean.CommunityRecommend

/**
 *@Author Stan
 *@Description
 *@Date 2025/4/11 10:24
 */
class CommendPagingSource(private val apiService: ApiService) :
    PagingSource<String, CommunityRecommend.Item>() {
    override fun getRefreshKey(state: PagingState<String, CommunityRecommend.Item>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, CommunityRecommend.Item> {
        return try {
            val page = params.key ?: ApiService.COMMUNITY_RECOMMEND_URL
            val repoResponse = apiService.getCommunityRecommend(page)
            val repoItem = repoResponse.itemList
            val prevKey = null
            val nextKey =
                if (repoItem.isNotEmpty() && !repoResponse.nextPageUrl.isNullOrEmpty()) repoResponse.nextPageUrl else null
            LoadResult.Page(repoItem, prevKey, nextKey)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }


    }
}