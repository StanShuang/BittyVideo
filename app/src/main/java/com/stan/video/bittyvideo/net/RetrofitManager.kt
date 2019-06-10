package com.stan.video.bittyvideo.net

import com.stan.video.bittyvideo.BuildConfig
import com.stan.video.bittyvideo.api.ApiService
import com.stan.video.bittyvideo.api.UrlConstant
import com.stan.video.bittyvideo.app.MyApplication
import com.stan.video.bittyvideo.net.intercepter.AddQueryParameterInterceptor
import com.stan.video.bittyvideo.net.intercepter.CacheInterceptor
import com.stan.video.bittyvideo.net.intercepter.HeaderInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by Stan
 * on 2019/6/10.
 */
object RetrofitManager {
    private var retrofit: Retrofit? = null
    val serviece: ApiService by lazy { getRetrofit()!!.create(ApiService :: class.java) }
    private fun getRetrofit(): Retrofit?{
        if(retrofit == null){
            synchronized(RetrofitManager ::class.java){
                if(retrofit == null){
                    retrofit =Retrofit.Builder()
                            .baseUrl(UrlConstant.BASE_URL)
                            .client(getOkHttpClient())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                }
            }
        }
        return retrofit
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
        val httpLoggingIntercerpter = HttpLoggingInterceptor()
        if(BuildConfig.DEBUG){
           httpLoggingIntercerpter.level = HttpLoggingInterceptor.Level.BODY
        }else{
            httpLoggingIntercerpter.level = HttpLoggingInterceptor.Level.NONE
        }
        val cacheFile = File(MyApplication.context.cacheDir,"cache")
        val cache = Cache(cacheFile,HttpConstant.MAX_CACHE_SIZE)
        builder.run {
            addInterceptor(httpLoggingIntercerpter)
            addInterceptor(HeaderInterceptor())
            addInterceptor(CacheInterceptor())
            addInterceptor(AddQueryParameterInterceptor())
            cache(cache)
            connectTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)//错误重连
        }
        return builder.build()


    }
}