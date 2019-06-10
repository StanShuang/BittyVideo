package com.stan.video.bittyvideo.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

/**
 * Created by Stan
 * on 2019/5/31.
 */
class AppUtils private constructor(){
    init {
        throw Error("Do not need instantiate!")
    }
    companion object {
        /**
         * 得到软件显示版本信息
         *
         * @param context 上下文
         * @return 当前版本信息
         */
        fun getVerName(context: Context): String {
            var verName = ""
            try {
                val packageName = context.packageName
                verName = context.packageManager
                        .getPackageInfo(packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return verName
        }
        fun getMobileModel(): String {
            var model: String? = Build.MODEL
            model = model?.trim { it <= ' ' } ?: ""
            return model
        }
    }
}