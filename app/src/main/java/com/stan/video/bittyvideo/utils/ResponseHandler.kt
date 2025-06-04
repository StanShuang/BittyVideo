package com.stan.video.bittyvideo.utils

import com.google.gson.JsonSyntaxException
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.utils.excetpion.ResponseCodeException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 *@Author Stan
 *@Description 获取网络请求返回的异常信息。
 *@Date 2025/5/22 15:48
 */
object ResponseHandler {
    fun getFailureTips(e: Throwable?) = when (e) {
        is ConnectException -> GlobalUtil.getString(R.string.network_connect_error)
        is SocketTimeoutException -> GlobalUtil.getString(R.string.network_connect_timeout)
        is ResponseCodeException -> GlobalUtil.getString(R.string.network_response_code_error) + e.responseCode
        is NoRouteToHostException -> GlobalUtil.getString(R.string.no_route_to_host)
        is UnknownHostException -> GlobalUtil.getString(R.string.network_error)
        is JsonSyntaxException -> GlobalUtil.getString(R.string.json_data_error)
        else -> {
            GlobalUtil.getString(R.string.unknown_error)
        }
    }
}