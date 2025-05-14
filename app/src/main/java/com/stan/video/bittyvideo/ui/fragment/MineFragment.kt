package com.stan.video.bittyvideo.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseViewFragment
import com.stan.video.bittyvideo.databinding.FragmentMineBinding
import com.stan.video.bittyvideo.ext.showToast
import com.stan.video.bittyvideo.ui.activity.ProfileHomePageActivity
import com.stan.video.bittyvideo.ui.activity.WatchHistoryActivity
import com.stan.video.bittyvideo.utils.StatusBarUtil

/**
 * Created by Stan
 * on 2019/6/6.
 */
class MineFragment : BaseViewFragment(), View.OnClickListener {
    private lateinit var binding: FragmentMineBinding
    override fun getLayoutView(): View {
        binding = FragmentMineBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mTitle: String? = null

    companion object {
        fun getInstance(title: String): MineFragment {
            val fragment = MineFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun lazyLoad() {

    }

    override fun initView() {
        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let { StatusBarUtil.setPaddingSmart(it, binding.toolbar) }
        binding.tvViewHomepage.setOnClickListener(this)
        binding.ivAvatar.setOnClickListener(this)
        binding.ivAbout.setOnClickListener(this)

        binding.tvCollection.setOnClickListener(this)
        binding.tvComment.setOnClickListener(this)

        binding.tvMineMessage.setOnClickListener(this)
        binding.tvMineAttention.setOnClickListener(this)
        binding.tvMineCache.setOnClickListener(this)
        binding.tvWatchHistory.setOnClickListener(this)
        binding.tvFeedback.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when {
            v?.id == R.id.iv_avatar || v?.id == R.id.tv_view_homepage -> {
                val intent = Intent(activity, ProfileHomePageActivity::class.java)
                startActivity(intent)
            }

            v?.id == R.id.iv_about -> showToast("关于我们")
            v?.id == R.id.tv_collection -> showToast("收藏")
            v?.id == R.id.tv_comment -> showToast("评论")
            v?.id == R.id.tv_mine_message -> showToast("我的消息")
            v?.id == R.id.tv_mine_attention -> showToast("我的关注")
            v?.id == R.id.tv_mine_attention -> showToast("我的缓存")
            v?.id == R.id.tv_watch_history -> startActivity(
                Intent(
                    activity,
                    WatchHistoryActivity::class.java
                )
            )

            v?.id == R.id.tv_feedback -> showToast("意见反馈")
        }
    }


}