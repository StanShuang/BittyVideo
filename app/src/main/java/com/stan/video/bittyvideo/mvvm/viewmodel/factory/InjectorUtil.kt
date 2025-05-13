package com.stan.video.bittyvideo.mvvm.viewmodel.factory

import com.stan.video.bittyvideo.mvvm.dao.MainPageRepository
import com.stan.video.bittyvideo.mvvm.net.NetworkRequestManager
import com.stan.video.bittyvideo.mvvm.net.PageDaoManager

/**
 *@Author Stan
 *@Description 应用程序逻辑控制管理类。
 *@Date 2025/4/9 16:00
 */
object InjectorUtil {
    private fun getMainPageRepository() = MainPageRepository.getInstance(
        PageDaoManager.getMainPageDao(),
        NetworkRequestManager.getInstance()
    )

    fun getSearchViewModelFactory() = SearchViewModelFactory(getMainPageRepository())

    fun getCommunityCommendViewModelFactory() = CommendViewModelFactory(getMainPageRepository())

    fun getFollowViewModelFactory() = FollowViewModelFactory(getMainPageRepository())
}