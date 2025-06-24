package com.stan.video.bittyvideo.utils

import android.app.Activity
import android.widget.ImageView
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import com.stan.video.bittyvideo.view.VideoListener
import com.stan.video.bittyvideo.view.load

/**
 *@Author Stan
 *@Description
 *@Date 2025/6/4 15:33
 */
object VideoAudioAutoPlayUtil {
    fun startAutoPlay(
        activity: Activity,
        player: GSYVideoPlayer,
        position: Int,
        playUrl: String,
        coverUrl: String,
        playTag: String,
        callBack: VideoListener? = null
    ) {
        player.run {
            //防止错位设置
            setPlayTag(playTag)
            //设置播放位置防止错位
            playPosition = position
            //音频焦点冲突时是否释放
//                setReleaseWhenLossAudio(false)
            //设置循环播放
            isLooping = true
            //增加封面
            val cover = ImageView(activity)
            cover.scaleType = ImageView.ScaleType.CENTER_CROP
            cover.load(coverUrl, 4f)
            cover.parent?.run { removeView(cover) }
            thumbImageView = cover
            //设置播放过程中的回调
            setVideoAllCallBack(callBack)
            //设置播放URL
            setUp(playUrl, false, null)
        }
    }
}