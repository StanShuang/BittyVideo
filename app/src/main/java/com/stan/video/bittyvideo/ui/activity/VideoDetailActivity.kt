package com.stan.video.bittyvideo.ui.activity

import android.annotation.TargetApi
import android.content.res.Configuration
import android.os.Build
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.transition.Transition
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.header.MaterialHeader
import com.shuyu.gsyvideoplayer.listener.LockClickListener
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseActivity
import com.stan.video.bittyvideo.ext.showToast
import com.stan.video.bittyvideo.glide.GlideApp
import com.stan.video.bittyvideo.mvp.contract.VideoDetailContract
import com.stan.video.bittyvideo.mvp.model.bean.HomeBean
import com.stan.video.bittyvideo.mvp.presenter.VideoDetailPresenter
import com.stan.video.bittyvideo.ui.adapter.VideoDetailAdapter
import com.stan.video.bittyvideo.utils.CleanLeakUtils
import com.stan.video.bittyvideo.utils.Constant
import com.stan.video.bittyvideo.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_video_detail.*
import com.stan.video.bittyvideo.recycleview.VideoListener
import java.text.SimpleDateFormat

/**
 * Created by Stan
 * on 2019/6/14.
 */
class VideoDetailActivity: BaseActivity() ,VideoDetailContract.View{
    companion object {
        const val IMG_TRANSITION = "IMG_TRANSITION"
        const val TRANSITION = "TRANSITION"
    }
    private val mPresenter by lazy { VideoDetailPresenter() }
    private val mAdapter by lazy { VideoDetailAdapter(this,itemList) }
    private val mFormat by lazy { SimpleDateFormat("yyyyMMddHHmmss"); }
    private var itemList = java.util.ArrayList<HomeBean.Issue.Item>()
    private lateinit var itemData: HomeBean.Issue.Item
    private var orientatinUtils: OrientationUtils? = null
    private var isTransition: Boolean = false
    private var isPlay: Boolean = false
    private var isPause: Boolean = false
    private var transition: Transition? = null
    private var mMaterialHeader: MaterialHeader? = null
    override fun layoutId(): Int = R.layout.activity_video_detail

    override fun initData() {
        itemData = intent.getSerializableExtra(Constant.BUNDLE_VIDEO_DATA) as HomeBean.Issue.Item
        isTransition = intent.getBooleanExtra(TRANSITION,false)
        //TODO 添加观看记录
    }

    override fun initView() {
        mPresenter.attachView(this)
        //过渡动画
        initTransition()
        //播放视频
        initVideoViewConifig()
        mRecyclerView.run {
            layoutManager = LinearLayoutManager(this@VideoDetailActivity)
            adapter = mAdapter
        }
        mAdapter.setOnItemDetailClick { mPresenter.loadVideoInfo(it) }
        //状态栏透明度以及间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this,mVideoView)



        mMaterialHeader = mRefreshLayout.refreshHeader as MaterialHeader
        //打开下拉刷新区背景
        mMaterialHeader?.setShowBezierWave(true)
        /***  下拉刷新  ***/
        //内容跟随偏移
        //设置下拉刷新主题颜色
        mRefreshLayout.run{
            setEnableHeaderTranslationContent(true)
            setPrimaryColorsId(R.color.color_light_black,R.color.color_title_bg)
            setOnRefreshListener{ loadVideoInfo() }
        }

    }

    /**
     * 初始化videoView 配置
     */
    private fun initVideoViewConifig() {
        //设置旋转
        orientatinUtils = OrientationUtils(this,mVideoView)
        //是否旋转
        mVideoView.isRotateViewAuto = false
        //是否可以滑动整体
        mVideoView.setIsTouchWiget(true)

        //增加封面
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        GlideApp.with(this)
                .load(itemData.data?.cover?.feed)
                .centerCrop()
                .into(imageView)
        mVideoView.thumbImageView = imageView
        mVideoView.setStandardVideoAllCallBack(object : VideoListener {
            override fun onPrepared(url: String, vararg objects: Any) {
                super.onPrepared(url, *objects)
                //开始播放后才能旋转屏幕
                mVideoView.isRotateViewAuto = true
                isPlay = true
            }
            override fun onAutoComplete(url: String, vararg objects: Any) {
                super.onAutoComplete(url, *objects)
                Logger.d("***** onAutoPlayComplete **** ")
            }

            override fun onPlayError(url: String, vararg objects: Any) {
                super.onPlayError(url, *objects)
                showToast("播放失败")
            }

            override fun onEnterFullscreen(url: String, vararg objects: Any) {
                super.onEnterFullscreen(url, *objects)
                Logger.d("***** onEnterFullscreen **** ")
            }

            override fun onQuitFullscreen(url: String, vararg objects: Any) {
                super.onQuitFullscreen(url, *objects)
                Logger.d("***** onQuitFullscreen **** ")
                //列表返回的样式判断
                orientatinUtils?.backToProtVideo()
            }

        })
        //设置返回按键
        mVideoView.backButton.setOnClickListener({onBackPressed()})
        //设置全屏按键功能
        mVideoView.fullscreenButton.setOnClickListener {
            //直接横屏
            orientatinUtils?.resolveByClick()
            //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
            mVideoView.startWindowFullscreen(this,true,true)
        }
        //锁屏事件
        mVideoView.setLockClickListener(object : LockClickListener{
            override fun onClick(view: View?, lock: Boolean) {
                //配合下方的onConfigurationChanged
                orientatinUtils?.isEnable = !lock
            }

        })


    }
    /**
     * 1.加载视频信息
     */
    fun loadVideoInfo() {
        mPresenter.loadVideoInfo(itemData)
    }
    private fun initTransition() {
        if(isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            postponeEnterTransition()
            ViewCompat.setTransitionName(mVideoView, IMG_TRANSITION)
            addTransitionListener()
            startPostponedEnterTransition()
        }else{
            loadVideoInfo()

        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun addTransitionListener() {
        transition = window.sharedElementEnterTransition
        transition?.addListener(object : Transition.TransitionListener{
            override fun onTransitionEnd(transition: Transition?) {
                loadVideoInfo()
                transition?.removeListener(this)

            }

            override fun onTransitionResume(transition: Transition?) {

            }

            override fun onTransitionPause(transition: Transition?) {

            }

            override fun onTransitionCancel(transition: Transition?) {

            }

            override fun onTransitionStart(transition: Transition?) {

            }

        })

    }

    override fun start() {

    }

    /**
     * 系统设置改变也可以触发
     */
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if(isPlay && !isPause){
            mVideoView.onConfigurationChanged(this,newConfig,orientatinUtils)
        }

    }

    /**
     * 返回事件
     */
    override fun onBackPressed() {
        orientatinUtils?.backToProtVideo()
        if(StandardGSYVideoPlayer.backFromWindowFull(this)){
            return
        }
        //释放所有
        mVideoView.setStandardVideoAllCallBack(null)
        GSYVideoPlayer.releaseAllVideos()
        if(isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) run{
            super.onBackPressed()
        }else{
            finish()
            overridePendingTransition(R.anim.anim_out,R.anim.anim_in)
        }

    }
    private fun getCurPlay(): GSYVideoPlayer{
        return if(mVideoView.fullWindowPlayer != null){
            mVideoView.fullWindowPlayer
        }else{
            mVideoView
        }
    }
    override fun onResume() {
        super.onResume()
        getCurPlay().onVideoResume()
        isPause  = false
    }

    override fun onPause() {
        super.onPause()
        getCurPlay().onVideoPause()
        isPause = true
    }
    override fun onDestroy() {
        CleanLeakUtils.fixInputMethodManagerLeak(this)
        super.onDestroy()
        GSYVideoPlayer.releaseAllVideos()
        orientatinUtils?.releaseListener()
        mPresenter.detachView()

    }
    override fun showLoading() {

    }

    override fun dismissLoading() {
        mRefreshLayout.finishRefresh()
    }
    /**
     * 设置播放视频 URL
     */
    override fun setVideo(url: String) {
        mVideoView.setUp(url,false,"")
        //开始自动播放
        mVideoView.startPlayLogic()
    }
    /**
     * 设置视频信息
     */
    override fun setVideoInfo(itemInfo: HomeBean.Issue.Item) {
        itemData = itemInfo
        mAdapter.addData(itemInfo)
        // 请求相关的最新等视频
        mPresenter.requestRelatedVideo(itemInfo.data?.id?:0)

    }
    /**
     * 设置背景颜色
     */
    override fun setBackground(url: String) {
        GlideApp.with(this)
                .load(url)
                .centerCrop()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .transition(DrawableTransitionOptions().crossFade())
                .into(mVideoBackground)
    }
    /**
     * 设置相关的数据视频
     */
    override fun setRecentRelatedVideo(itemList: ArrayList<HomeBean.Issue.Item>) {
        mAdapter.addData(itemList)
        this.itemList = itemList
    }

    override fun setErrorMsg(errorMsg: String) {

    }
}