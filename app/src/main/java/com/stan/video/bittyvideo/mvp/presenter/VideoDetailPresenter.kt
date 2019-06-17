package com.stan.video.bittyvideo.mvp.presenter

import android.app.Activity
import com.stan.video.bittyvideo.app.MyApplication
import com.stan.video.bittyvideo.base.BasePresenter
import com.stan.video.bittyvideo.ext.dataFormat
import com.stan.video.bittyvideo.ext.showToast
import com.stan.video.bittyvideo.mvp.contract.VideoDetailContract
import com.stan.video.bittyvideo.mvp.model.VideoDetailModel
import com.stan.video.bittyvideo.mvp.model.bean.HomeBean
import com.stan.video.bittyvideo.net.exception.ExceptionHandle
import com.stan.video.bittyvideo.utils.DisplayManager
import com.stan.video.bittyvideo.utils.NetWorkUtil

/**
 * Created by Stan
 * on 2019/6/17.
 */
class VideoDetailPresenter: BasePresenter<VideoDetailContract.View>(),VideoDetailContract.Presenter {
    private val videoDetailModel:  VideoDetailModel by lazy {
        VideoDetailModel()
    }
    /**
     * 加载视频相关的数据
     */
    override fun loadVideoInfo(itemInfo: HomeBean.Issue.Item) {
        val playInfo = itemInfo.data?.playInfo
        val netType = NetWorkUtil.isWifi(MyApplication.context)
        //检查是否绑定view
        checkViewAttached()
        if(playInfo!!.size > 1){
            //wafi网络环境选择高清
            if(netType){
                for(i in playInfo){
                    if(i.type == "high"){
                        val playUri = i.url
                        mRootView?.setVideo(playUri)
                        break
                    }
                }

            }else{
                for(i in playInfo){
                    if(i.type == "normal"){
                        val playUri = i.url
                        mRootView?.setVideo(playUri)
                        (mRootView as Activity).showToast("本次消耗${(mRootView as Activity).dataFormat(i.urlList[0].size)}流量")
                        break
                    }
                }

            }
        }else{
            mRootView?.setVideo(itemInfo.data.playUrl)
        }
        //设置背景
        val backgroundUrl = itemInfo.data.cover.blurred + "/thumbnail/${DisplayManager.getScreenHeight()!! - DisplayManager.dip2px(250f)!!}x${DisplayManager.getScreenWidth()}"
        backgroundUrl.let { mRootView?.setBackground(it) }

        mRootView?.setVideoInfo(itemInfo)

    }
    /**
     * 请求相关的视频数据
     */
    override fun requestRelatedVideo(id: Long) {
        mRootView?.showLoading()
        val disposable = videoDetailModel.requestRelatedData(id)
                .subscribe({ issue ->
                    mRootView?.apply {
                        dismissLoading()
                        setRecentRelatedVideo(issue.itemList)
                    }
                },{ t ->
                    mRootView?.apply {
                        dismissLoading()
                        setErrorMsg(ExceptionHandle.handleException(t))
                    }

                })

        addSubscription(disposable)
    }
}