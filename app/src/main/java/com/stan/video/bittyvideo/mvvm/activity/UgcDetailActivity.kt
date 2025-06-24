package com.stan.video.bittyvideo.mvvm.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.gyf.immersionbar.ImmersionBar
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView.releaseAllVideos
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.databinding.ActivityUgcDetailBinding
import com.stan.video.bittyvideo.mvp.model.bean.CommunityRecommend
import com.stan.video.bittyvideo.mvvm.adapter.UgcDetailAdapter
import com.stan.video.bittyvideo.mvvm.viewmodel.UgcDetailViewModel
import com.stan.video.bittyvideo.utils.GlobalUtil
import com.stan.video.bittyvideo.utils.IntentDataHolderUtil
import com.stan.video.bittyvideo.utils.callback.AutoPlayPageChangeListener
import com.stan.video.bittyvideo.utils.showToast


class UgcDetailActivity : AppCompatActivity() {
    private var _binding: ActivityUgcDetailBinding? = null
    private val binding: ActivityUgcDetailBinding
        get() = _binding!!
    private val viewModel by lazy { ViewModelProvider(this)[UgcDetailViewModel::class.java] }
    private var onPageChangeCallback: ViewPager2.OnPageChangeCallback? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        _binding = ActivityUgcDetailBinding.inflate(layoutInflater)
        //判断传递参数不为null
        if (IntentDataHolderUtil.getData<List<CommunityRecommend.Item>>(
                EXTRA_RECOMMEND_ITEM_LIST_JSON
            ).isNullOrEmpty()
            || IntentDataHolderUtil.getData<CommunityRecommend.Item>(EXTRA_RECOMMEND_ITEM_JSON) == null
        ) {
            GlobalUtil.getString(R.string.jump_page_unknown_error).showToast()
            finish()
        } else {
            setContentView(binding.root)
            setStatusBarBackground(R.color.black)
        }
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        if (viewModel.dataList == null) {
            viewModel.itemPosition = getCurrentItemPosition()
            viewModel.dataList = IntentDataHolderUtil.getData<List<CommunityRecommend.Item>>(
                EXTRA_RECOMMEND_ITEM_LIST_JSON
            )
        }

        if (viewModel.dataList == null) {
            GlobalUtil.getString(R.string.jump_page_unknown_error).showToast()
            finish()
        } else {
            binding.viewPager.adapter = UgcDetailAdapter(this, viewModel.dataList!!)
            binding.viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
            binding.viewPager.offscreenPageLimit = 1
            onPageChangeCallback = AutoPlayPageChangeListener(
                binding.viewPager,
                viewModel.itemPosition,
                R.id.videoPlayer
            )
            binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback!!)
            binding.viewPager.setCurrentItem(viewModel.itemPosition, false)
        }

    }

    private fun getCurrentItemPosition(): Int {
        val list = IntentDataHolderUtil.getData<List<CommunityRecommend.Item>>(
            EXTRA_RECOMMEND_ITEM_LIST_JSON
        )
        val currentItem =
            IntentDataHolderUtil.getData<CommunityRecommend.Item>(EXTRA_RECOMMEND_ITEM_JSON)
        list?.forEachIndexed { index, item ->
            if (currentItem == item) {
                viewModel.itemPosition = index
                return@forEachIndexed
            }

        }
        return viewModel.itemPosition
    }

    private fun setStatusBarBackground(statusBarColor: Int) {
        ImmersionBar.with(this).autoStatusBarDarkModeEnable(true, 0.2f)
            .statusBarColor(statusBarColor).fitsSystemWindows(true).init()
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseAllVideos()
        onPageChangeCallback?.run { binding.viewPager.unregisterOnPageChangeCallback(this) }
        onPageChangeCallback = null
        _binding = null
    }

    companion object {
        const val TAG = "UgcDetailActivity"
        const val EXTRA_RECOMMEND_ITEM_LIST_JSON = "recommend_item_list"
        const val EXTRA_RECOMMEND_ITEM_JSON = "recommend_item"
        fun start(
            context: Activity,
            dataList: List<CommunityRecommend.Item>,
            currentItem: CommunityRecommend.Item
        ) {
            IntentDataHolderUtil.setData(EXTRA_RECOMMEND_ITEM_LIST_JSON, dataList)
            IntentDataHolderUtil.setData(EXTRA_RECOMMEND_ITEM_JSON, currentItem)
            val starter = Intent(context, UgcDetailActivity::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                context,
                R.anim.ugc_push_up_in,
                R.anim.ugc_push_up_out
            )
            context.startActivity(starter, options.toBundle())
        }
    }
}