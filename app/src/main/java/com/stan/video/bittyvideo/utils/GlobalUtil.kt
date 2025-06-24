package com.stan.video.bittyvideo.utils

import android.app.Activity
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.stan.video.bittyvideo.app.MyApplication
import com.stan.video.bittyvideo.ui.fragment.ShareDialogFragment

/**
 *@Author Stan
 *@Description
 *@Date 2025/4/10 14:59
 */
object GlobalUtil {
    /**
     * 获取资源文件中定义的字符串。
     *
     * @param resId
     * 字符串资源id
     * @return 字符串资源id对应的字符串内容。
     */
    fun getString(resId: Int): String = MyApplication.context.resources.getString(resId)

    /**
     * 批量设置控件点击事件。
     *
     * @param v 点击的控件
     * @param block 处理点击事件回调代码块
     */
    fun setOnClickListener(vararg v: View?, block: View.() -> Unit) {
        val listener = View.OnClickListener { it.block() }
        v.forEach { it?.setOnClickListener(listener) }
    }

    /**
     * 获取资源文件中定义的字符串。
     *
     * @param resId
     * 字符串资源id
     * @return 字符串资源id对应的字符串内容。
     */
    fun getDimension(resId: Int): Int =
        MyApplication.context.resources.getDimensionPixelOffset(resId)

    /**
     * 获取当前应用程序的名称。
     * @return 当前应用程序的名称。
     */
    val appName: String
        get() = MyApplication.context.resources.getString(MyApplication.context.applicationInfo.labelRes)

    /**
     * 弹出分享对话框。
     *
     * @param activity 上下文
     * @param shareContent 分享内容
     */
    fun showDialogShare(activity: Activity, shareContent: String) {
        if (activity is AppCompatActivity) {
            ShareDialogFragment().showDialog(activity, shareContent)
        }
    }

    /**
     * 调用系统原生分享。
     *
     * @param activity 上下文
     * @param shareContent 分享内容
     * @param shareType SHARE_MORE=0，SHARE_QQ=1，SHARE_WECHAT=2，SHARE_WEIBO=3，SHARE_QQZONE=4
     */
    fun share(activity: Activity, shareContent: String, shareType: Int) {
        ShareUtil.share(activity, shareContent, shareType)
    }

    /**
     * 判断某个应用是否安装。
     * @param packageName
     * 要检查是否安装的应用包名
     * @return 安装返回true，否则返回false。
     */
    fun isInstalled(packageName: String): Boolean {
        val packageInfo: PackageInfo? = try {
            MyApplication.context.packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
        return packageInfo != null
    }

    /**
     * 判断手机是否安装了QQ。
     */
    fun isQQInstalled() = isInstalled("com.tencent.mobileqq")

    /**
     * 判断手机是否安装了微信。
     */
    fun isWechatInstalled() = isInstalled("com.tencent.mm")

    /**
     * 判断手机是否安装了微博。
     * */
    fun isWeiboInstalled() = isInstalled("com.sina.weibo")
}