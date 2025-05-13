package com.stan.video.bittyvideo.ui.activity

import android.annotation.SuppressLint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import android.webkit.WebView
import android.webkit.WebViewClient
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.scwang.smartrefresh.layout.util.DensityUtil
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseViewActivity
import com.stan.video.bittyvideo.databinding.ActivityProfileHomepageBinding
import com.stan.video.bittyvideo.utils.CleanLeakUtils
import com.stan.video.bittyvideo.utils.StatusBarUtil
import java.util.*

/**
 * Created by Stan
 * on 2019/7/4.
 */
class ProfileHomePageActivity : BaseViewActivity() {
    private lateinit var binding: ActivityProfileHomepageBinding
    override fun layoutView(): View {
        binding = ActivityProfileHomepageBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mOffset = 0
    private var mScrollY = 0
    override fun initData() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, binding.toolbar)
        binding.refreshlayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            //下拉
            override fun onHeaderPulling(
                header: RefreshHeader?,
                percent: Float,
                offset: Int,
                headerHeight: Int,
                extendHeight: Int
            ) {
                mOffset = offset / 2
                binding.parallax.translationY = (mOffset - mScrollY).toFloat()
                binding.toolbar.alpha = 1 - percent.coerceAtMost(1f)
//                Log.d("stan-bittyvideo","mOffset="+mOffset+";parallax.translationY="+parallax.translationY+";toolbar.alpha="+ toolbar.alpha)
            }

            //恢复
            override fun onHeaderReleasing(
                header: RefreshHeader?,
                percent: Float,
                offset: Int,
                footerHeight: Int,
                extendHeight: Int
            ) {
                mOffset = offset / 2
                binding.parallax.translationY = (mOffset - mScrollY).toFloat()
                binding.toolbar.alpha = 1 - percent.coerceAtMost(1f)
//                Log.d("stan-bittyvideo","mOffset="+mOffset+";parallax.translationY="+parallax.translationY+";toolbar.alpha="+ toolbar.alpha)
            }
        })

        binding.scrollView.setOnScrollChangeListener(object :
            NestedScrollView.OnScrollChangeListener {
            private var lastScrollY = 0
            private val h = DensityUtil.dp2px(170f)
            private val color =
                ContextCompat.getColor(applicationContext, R.color.colorPrimary) and 0x00ffffff

            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                var tScrollY = scrollY
                if (lastScrollY < h) {
                    tScrollY = Math.min(h, tScrollY)
                    mScrollY = if (tScrollY > h) h else tScrollY
                    binding.buttonBarLayout.alpha = 1f * mScrollY / h
                    binding.toolbar.setBackgroundColor(255 * mScrollY / h shl 24 or color)
                    binding.parallax.translationY = (mOffset - mScrollY).toFloat()
                }
                lastScrollY = tScrollY
            }
        })

        binding.buttonBarLayout.alpha = 0f
        binding.toolbar.setBackgroundColor(0)
        //返回
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.refreshlayout.setOnRefreshListener { binding.mWebView.loadUrl("https://xuhaoblog.com/KotlinMvp") }
        binding.refreshlayout.autoRefresh()

        binding.mWebView.settings.javaScriptEnabled = true
        binding.mWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                binding.refreshlayout.finishRefresh()
                view.loadUrl(
                    String.format(
                        Locale.CHINA,
                        "javascript:document.body.style.paddingTop='%fpx'; void 0",
                        DensityUtil.px2dp(binding.mWebView.paddingTop.toFloat())
                    )
                )
            }
        }

    }

    override fun start() {

    }

    override fun onDestroy() {
        CleanLeakUtils.fixInputMethodManagerLeak(this)
        super.onDestroy()
    }
}