package com.stan.video.bittyvideo.glide

import android.content.Context
import android.view.inputmethod.InputMethod
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule
import java.io.InputStream

/**
 * Created by Stan
 * on 2019/6/14.
 */
@GlideModule
class CustomAppGlideModule: AppGlideModule() {
    /**
     * 通过GlideBuilder设置默认的结构(Engine,BitmapPool ,ArrayPool,MemoryCache等等).
     */
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        /*重新设置内存限制*/
        builder.setMemoryCache(LruResourceCache(10*1024*1024))
    }
    /**
     * 清单解析的开启
     *
     *
     * 这里不开启，避免添加相同的modules两次
     *
     * @return
     */
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(String :: class.java,InputStream :: class.java,CustomBaseGlideUrlLoader.Factory())
    }
}