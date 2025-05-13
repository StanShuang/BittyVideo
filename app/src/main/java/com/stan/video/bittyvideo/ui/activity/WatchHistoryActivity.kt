package com.stan.video.bittyvideo.ui.activity

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseViewActivity
import com.stan.video.bittyvideo.databinding.LayoutWatchHistoryBinding
import com.stan.video.bittyvideo.mvp.model.bean.NewWatchHistoryBean

import com.stan.video.bittyvideo.ui.adapter.WatchHistoryAdapter
import com.stan.video.bittyvideo.utils.StatusBarUtil
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.litepal.LitePal

/**
 * Created by Stan
 * on 2019/7/4.
 */
class WatchHistoryActivity : BaseViewActivity() {
    private lateinit var binding: LayoutWatchHistoryBinding
    override fun layoutView(): View {
        binding = LayoutWatchHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    private var historys = ArrayList<NewWatchHistoryBean>()
    private val mAdapter by lazy {
        WatchHistoryAdapter(
            this,
            historys,
            R.layout.item_video_small_card
        )
    }

    override fun initData() {
        doAsync {
            val historyBeans = LitePal.findAll(NewWatchHistoryBean::class.java)
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
        if (historys.isNotEmpty()) {
            binding.multipleStatusView.showContent()
        } else {
            binding.multipleStatusView.showEmpty()
        }
        mAdapter.addData(historys)
    }


    override fun initView() {
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.mRecyclerView.run {
            layoutManager = LinearLayoutManager(this@WatchHistoryActivity)
            adapter = mAdapter
        }
        if (historys.isNotEmpty()) {
            binding.multipleStatusView.showContent()
        } else {
            binding.multipleStatusView.showEmpty()
        }
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, binding.toolbar)
        StatusBarUtil.setPaddingSmart(this, binding.mRecyclerView)

    }

    override fun start() {

    }


}