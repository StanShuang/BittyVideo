package com.stan.video.bittyvideo.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.databinding.FragmentShareDialogBinding
import com.stan.video.bittyvideo.utils.GlobalUtil.share
import com.stan.video.bittyvideo.utils.SHARE_MORE
import com.stan.video.bittyvideo.utils.SHARE_QQ
import com.stan.video.bittyvideo.utils.SHARE_QQZONE
import com.stan.video.bittyvideo.utils.SHARE_WECHAT
import com.stan.video.bittyvideo.utils.SHARE_WEIBO
import com.stan.video.bittyvideo.view.setDrawable

/**
 *@Author Stan
 *@Description 分享对话框的弹出界面。
 *@Date 2025/6/12 16:29
 */
class ShareDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentShareDialogBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var shareContent: String
    private lateinit var attachedActivity: Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShareDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            attachedActivity = it
            binding.tvToWechatFriends.setDrawable(
                ContextCompat.getDrawable(
                    it,
                    R.drawable.ic_share_wechat_black_30dp
                ), 30f, 30f, 1
            )
            binding.tvShareToWeibo.setDrawable(
                ContextCompat.getDrawable(
                    it,
                    R.drawable.ic_share_weibo_black_30dp
                ), 30f, 30f, 1
            )
            binding.tvShareToQQ.setDrawable(
                ContextCompat.getDrawable(
                    it,
                    R.drawable.ic_share_qq_black_30dp
                ), 30f, 30f, 1
            )
            binding.tvShareToQQzone.setDrawable(
                ContextCompat.getDrawable(
                    it,
                    R.drawable.ic_share_qq_zone_black_30dp
                ), 30f, 30f, 1
            )
            binding.tvToWechatFriends.setOnClickListener {
                share(attachedActivity, shareContent, SHARE_WECHAT)
                dismiss()
            }
            binding.tvShareToQQ.setOnClickListener {
                share(attachedActivity, shareContent, SHARE_QQ)
                dismiss()
            }
            binding.tvShareToWeibo.setOnClickListener {
                share(attachedActivity, shareContent, SHARE_WEIBO)
                dismiss()
            }
            binding.tvShareToQQzone.setOnClickListener {
                share(attachedActivity, shareContent, SHARE_QQZONE)
                dismiss()
            }
            binding.llMore.setOnClickListener {
                share(attachedActivity, shareContent, SHARE_MORE)
                dismiss()
            }

            binding.tvCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showDialog(activity: AppCompatActivity, string: String) {
        show(activity.supportFragmentManager, "share_dialog")
        this.shareContent = string
    }
}