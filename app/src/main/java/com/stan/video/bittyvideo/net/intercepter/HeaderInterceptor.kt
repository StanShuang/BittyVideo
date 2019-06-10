package com.stan.video.bittyvideo.net.intercepter


import com.stan.video.bittyvideo.utils.Preference
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Stan
 * on 2019/6/10.
 */
class HeaderInterceptor: Interceptor {
    private var token:String by Preference("token","")
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.header("token",token)
        builder.method(request.method(),request.body())
        return chain.proceed(builder.build())
    }
}