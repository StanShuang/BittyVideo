package com.stan.video.bittyvideo.net.intercepter

import com.stan.video.bittyvideo.utils.AppUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Stan
 * on 2019/6/10.
 */
class AddQueryParameterInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        val modifiedUrl = request.url().newBuilder()
                .addQueryParameter("udid", "d2807c895f0348a180148c9dfa6f2feeac0781b5")
                .addQueryParameter("deviceModel", AppUtils.getMobileModel())
                .build()
        return chain.proceed(builder.url(modifiedUrl).build())
    }
}