package com.stan.video.bittyvideo.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.app.MyApplication
import com.stan.video.bittyvideo.base.BaseActivity
import com.stan.video.bittyvideo.base.BaseViewActivity
import com.stan.video.bittyvideo.databinding.ActivitySplashBinding
import com.stan.video.bittyvideo.utils.AppUtils
import com.stan.video.bittyvideo.utils.StatusBarUtil
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by Stan
 * on 2019/5/31.
 * 启动页
 */
class SplashActivity : BaseViewActivity() {
    private var textTypeFace: Typeface? = null
    private var descTypeFace: Typeface? = null
    private var alphaAnimation: AlphaAnimation? = null
    private lateinit var binding: ActivitySplashBinding
    override fun layoutView(): View {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        return binding.root
    }

    init {
        textTypeFace =
            Typeface.createFromAsset(MyApplication.context.assets, "fonts/Lobster-1.4.otf")
        descTypeFace = Typeface.createFromAsset(
            MyApplication.context.assets,
            "fonts/FZLanTingHeiS-L-GB-Regular.TTF"
        )
    }

    override fun initData() {

    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, binding.layoutSplash)
        binding.tvAppName.typeface = textTypeFace
        binding.tvSplashDesc.typeface = descTypeFace
        binding.tvVersionName.text = "v${AppUtils.getVerName(MyApplication.context)}"
        alphaAnimation = AlphaAnimation(0.3f, 1.0f)
        alphaAnimation?.duration = 2000
        alphaAnimation?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                redirectTo()
            }

            override fun onAnimationStart(animation: Animation?) {

            }

        })
        checkPermission()
    }

    private fun redirectTo() {
        Intent(this, MainActivity::class.java).run {
            startActivity(this)
        }
        finish()
    }

    private fun checkPermission() {
//        val perms = arrayOf(Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val perms = arrayOf(Manifest.permission.READ_PHONE_STATE)
        EasyPermissions.requestPermissions(this, "Bitty Vido需要以下权限，请允许", 0, *perms)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == 0) {
            if (perms.isNotEmpty()) {
                if (perms.contains(Manifest.permission.READ_PHONE_STATE)) {
                    if (alphaAnimation != null) {
                        binding.ivWebIcon.startAnimation(alphaAnimation)
                    }
                }
            }
        }
    }

    override fun start() {

    }
}