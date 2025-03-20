package com.stan.video.bittyvideo.ui.activity

import android.os.Looper
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseActivity
import com.stan.video.bittyvideo.mvp.model.bean.NewWatchHistoryBean

import com.stan.video.bittyvideo.mvp.model.bean.WatchHistoryBean
import com.stan.video.bittyvideo.ui.adapter.WatchHistoryAdapter
import com.stan.video.bittyvideo.utils.StatusBarUtil
import kotlinx.android.synthetic.main.layout_watch_history.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.litepal.LitePal

/**
 * Created by Stan
 * on 2019/7/4.
 */
class WatchHistoryActivity: BaseActivity() {

    override fun layoutId(): Int = R.layout.layout_watch_history
    private var historys =ArrayList<NewWatchHistoryBean>()
    private val mAdapter by lazy{ WatchHistoryAdapter(this, historys, R.layout.item_video_small_card) }
    override fun initData() {
        doAsync {
            val historyBeans = LitePal.findAll(NewWatchHistoryBean ::class.java)
            historyBeans.reverse()
            historyBeans.forEach { it ->
                Log.d("litepal", "vedio title is ${it.title} , vedio time is ${it.category}")
                it.playInfo.forEach {
                    Log.d("litepal", "playInfo nzme is ${it.name} , vedio type is ${it.type}")
                }
            }
            historys = historyBeans as ArrayList<NewWatchHistoryBean>
            uiThread {
                setRecyclerView()
            }
        }
    }

    private fun setRecyclerView() {
        if(historys.size > 0){
            multipleStatusView.showContent()
        }else{
            multipleStatusView.showEmpty()
        }
        mAdapter.addData(historys)
    }


    override fun initView() {
        toolbar.setNavigationOnClickListener { finish() }
        mRecyclerView.run {
            layoutManager = LinearLayoutManager(this@WatchHistoryActivity)
            adapter = mAdapter
        }
        if(historys.size > 0){
            multipleStatusView.showContent()
        }else{
            multipleStatusView.showEmpty()
        }
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this,toolbar)
        StatusBarUtil.setPaddingSmart(this,mRecyclerView)

    }

    override fun start() {

    }


}