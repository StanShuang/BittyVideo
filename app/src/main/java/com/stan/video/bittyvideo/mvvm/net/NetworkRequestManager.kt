package com.stan.video.bittyvideo.mvvm.net

import com.stan.video.bittyvideo.net.RetrofitManager

/**
 *@Author Stan
 *@Description 管理所有网络请求。
 *@Date 2025/4/10 10:16
 */
class NetworkRequestManager {
    var mainPageService = RetrofitManager.serviece

    suspend fun fetchHotSearch() = RetrofitManager.serviece.getHotSearch()


    companion object {
        @Volatile
        private var INSTANCE: NetworkRequestManager? = null

        fun getInstance(): NetworkRequestManager = INSTANCE ?: synchronized(this) {
            INSTANCE ?: NetworkRequestManager()
        }
    }
}