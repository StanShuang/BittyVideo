package com.stan.video.bittyvideo.utils.callback

import android.app.AlertDialog
import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shuyu.gsyvideoplayer.R
import com.shuyu.gsyvideoplayer.utils.CommonUtil
import com.shuyu.gsyvideoplayer.utils.NetworkUtils
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer
import com.stan.video.bittyvideo.view.AutoPlayerVideoPlayer
import com.stan.video.bittyvideo.view.dp2px
import com.stan.video.bittyvideo.view.screenHeight

/**
 *@Author Stan
 *@Description RecyclerView列表自动播放监听类。
 *@Date 2025/5/22 16:56
 */
class AutoPlayScrollListener(
    private val itemPlayId: Int,
    private val rangeTop: Int = PLAY_RANGE_TOP,
    private val rangeBottom: Int = PLAY_RANGE_BOTTOM
) : RecyclerView.OnScrollListener() {
    private var firstVisible = 0
    private var lastVisible = 0
    private var visibleCount = 0
    private var runnable: PlayRunnable? = null
    private val playHandler = Handler()

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            playVideo(recyclerView)
        }
    }


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val firstVisibilityItem =
            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val lastVisibilityItem =
            (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

        if (firstVisible == firstVisibilityItem) return
        firstVisible = firstVisibilityItem
        lastVisible = lastVisibilityItem
        visibleCount = lastVisibilityItem - firstVisibilityItem
    }

    private fun playVideo(view: RecyclerView?) {
        view?.run {
            val layoutManager = view.layoutManager
            var gsyBaseVideoPlayer: GSYBaseVideoPlayer? = null
            var needPlay = false
            for (i in 0 until visibleCount) {
                if (layoutManager!!.getChildAt(i) != null && layoutManager.getChildAt(i)!!
                        .findViewById<View>(itemPlayId) != null
                ) {
                    val player = layoutManager.getChildAt(i)!!
                        .findViewById<View>(itemPlayId) as GSYBaseVideoPlayer
                    val rect = Rect()
                    player.getLocalVisibleRect(rect)
                    val height = player.height
                    //说明第一个完全可视
                    if (rect.top == 0 && rect.bottom == height) {
                        gsyBaseVideoPlayer = player
                        if (player.currentPlayer.currentState == GSYBaseVideoPlayer.CURRENT_STATE_NORMAL || player.currentPlayer.currentState == GSYBaseVideoPlayer.CURRENT_STATE_ERROR) {
                            needPlay = true
                        }
                        break
                    }

                }
            }
            if (gsyBaseVideoPlayer != null && needPlay) {
                runnable?.let {
                    val tmpPlayer = it.gsyBaseVideoPlayer
                    playHandler.removeCallbacks(it)
                    runnable = null
                    if (tmpPlayer === gsyBaseVideoPlayer) return
                }
                runnable = PlayRunnable(gsyBaseVideoPlayer, rangeTop, rangeBottom)
                //降低频率
                playHandler.postDelayed(runnable!!, 400)
            }
        }
    }

    private class PlayRunnable(
        val gsyBaseVideoPlayer: GSYBaseVideoPlayer?,
        val rangeTop: Int,
        val rangeBottom: Int
    ) : Runnable {
        var isNeedShowWifiDialog: Boolean = false
        override fun run() {
            var isPosition = false
            //如果未播放，需要播放
            if (gsyBaseVideoPlayer != null) {
                val screenPosition = IntArray(2)
                //方法通常用来获取一个视图（View）在当前窗口中的绝对坐标位置
                gsyBaseVideoPlayer.getLocationOnScreen(screenPosition)
                val halfHeight = gsyBaseVideoPlayer.height / 2
                val rangePosition = screenPosition.last() + halfHeight
                //中心点在播放区域内
                if (rangePosition >= rangeTop && rangePosition <= rangeBottom) {
                    isPosition = true
                }
                if (isPosition) {
                    //判断一下网络

                    if (!CommonUtil.isWifiConnected(gsyBaseVideoPlayer.context) && isNeedShowWifiDialog) {
                        showWifiDialog(gsyBaseVideoPlayer, gsyBaseVideoPlayer.context)
                        return
                    }
                    gsyBaseVideoPlayer.startPlayLogic()
                }
            }

        }

        private fun showWifiDialog(gsyBaseVideoPlayer: GSYBaseVideoPlayer, context: Context) {
            if (!NetworkUtils.isAvailable(context)) {
                Toast.makeText(
                    context,
                    context.resources.getString(R.string.no_net),
                    Toast.LENGTH_LONG
                ).show()
                return
            }
            AlertDialog.Builder(context).apply {
                setMessage(context.resources.getString(R.string.tips_not_wifi))
                setPositiveButton(context.resources.getString(R.string.tips_not_wifi_confirm)) { dialog, which ->
                    dialog.dismiss()
                    gsyBaseVideoPlayer.startPlayLogic()
                    isNeedShowWifiDialog = false
                }
                setPositiveButton(context.resources.getString(R.string.tips_not_wifi_confirm)) { dialog, which ->
                    dialog.dismiss()
                    gsyBaseVideoPlayer.startPlayLogic()
                    isNeedShowWifiDialog = false
                }
                setNegativeButton(context.resources.getString(R.string.tips_not_wifi_cancel)) { dialog, which ->
                    dialog.dismiss()
                    isNeedShowWifiDialog = true
                }
                create()
            }.show()
        }

    }


    companion object {
        /**
         * 指定自动播放，在屏幕上的区域范围，上
         */
        val PLAY_RANGE_TOP = screenHeight / 2 - dp2px(180f)

        /**
         * 指定自动播放，在屏幕上的区域范围，下
         */

        val PLAY_RANGE_BOTTOM = screenHeight / 2 + dp2px(180f)
    }
}