package com.stan.video.bittyvideo.mvvm.net

/**
 *@Author Stan
 *@Description 应用程序所有Dao操作管理类。
 *@Date 2025/4/10 10:33
 */
object PageDaoManager {
    private lateinit var mainPageDao: MainPageDao

    fun getMainPageDao() = if (this::mainPageDao.isInitialized) mainPageDao else MainPageDao()
}