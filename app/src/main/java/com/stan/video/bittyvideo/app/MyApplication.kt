package com.stan.video.bittyvideo.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.multidex.MultiDexApplication
import android.util.Log
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.squareup.leakcanary.BuildConfig
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import com.stan.video.bittyvideo.utils.DisplayManager
import org.litepal.LitePal
import kotlin.properties.Delegates

/**
 * Created by Stan
 * on 2019/5/31.
 */
class MyApplication: MultiDexApplication() {
    private var refWatcher: RefWatcher? = null
    companion object {
        private val TAG = "MyApplication"
        var context: Context by Delegates.notNull()
            private set
        fun getRefWatcher(context: Context): RefWatcher?{
            val myApplicaction = context.applicationContext as MyApplication
            return myApplicaction.refWatcher
        }
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        refWatcher = setupLeakCanary()
        initConfig()
        LitePal.initialize(this)
        DisplayManager.init(this)
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
    }

    /**
     * 初始化设置，日志打印
     */
    private fun initConfig() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false) // 隐藏线程信息 默认：显示
                .methodCount(0)         // 决定打印多少行（每一行代表一个方法）默认：2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("hao_zz")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy){
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }

    private fun setupLeakCanary(): RefWatcher? {
        return if(LeakCanary.isInAnalyzerProcess(this)){
            RefWatcher.DISABLED
        }else{
            LeakCanary.install(this)
        }
    }
    private val mActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks{
        override fun onActivityPaused(activity: Activity?) {

        }

        override fun onActivityResumed(activity: Activity?) {

        }

        override fun onActivityStarted(activity: Activity) {
            Log.d(TAG, "onStart: " + activity.componentName.className)
        }

        override fun onActivityDestroyed(activity: Activity) {
            Log.d(TAG, "onDestroy: " + activity.componentName.className)
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

        }

        override fun onActivityStopped(activity: Activity?) {

        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            Log.d(TAG, "onCreated: " + activity.componentName.className)
        }

    }
}