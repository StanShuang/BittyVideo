package com.stan.video.bittyvideo.ui.activity

import android.Manifest
import android.content.Intent
import android.graphics.Typeface
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.app.MyApplication
import com.stan.video.bittyvideo.base.BaseActivity
import com.stan.video.bittyvideo.utils.AppUtils
import kotlinx.android.synthetic.main.activity_splash.*
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by Stan
 * on 2019/5/31.
 * 启动页
 */
class SplashActivity:BaseActivity() {
    private var textTypeFace: Typeface? = null
    private var descTypeFace: Typeface? = null
    private var alphaAnimation: AlphaAnimation? = null
    override fun layoutId(): Int = R.layout.activity_splash
    init {
        textTypeFace = Typeface.createFromAsset(MyApplication.context.assets,"fonts/lobster-1.4.otf")
        descTypeFace = Typeface.createFromAsset(MyApplication.context.assets,"fonts/FZLanTingHeiS-L-GB-Regular.TTF")
    }

    override fun initData() {

    }

    override fun initView() {
        tv_app_name.typeface = textTypeFace
        tv_splash_desc.typeface = descTypeFace
        tv_version_name.text = "v${AppUtils.getVerName(MyApplication.context)}"
        alphaAnimation = AlphaAnimation(0.3f,1.0f)
        alphaAnimation?.duration = 2000
        alphaAnimation?.setAnimationListener(object : Animation.AnimationListener{
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
        Intent(this,MainActivity :: class.java).run {
            startActivity(this)
        }
        finish()
    }
    private fun checkPermission(){
        val perms = arrayOf(Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
        EasyPermissions.requestPermissions(this,"Bitty Vido需要以下权限，请允许",0,*perms)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if(requestCode == 0){
            if(perms.isNotEmpty()){
                if(perms.contains(Manifest.permission.READ_PHONE_STATE)&&perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    if(alphaAnimation != null){
                        iv_web_icon.startAnimation(alphaAnimation)
                    }
                }
            }
        }
    }
    override fun start() {

    }
}