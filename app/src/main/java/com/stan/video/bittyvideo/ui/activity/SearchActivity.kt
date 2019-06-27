package com.stan.video.bittyvideo.ui.activity

import android.annotation.TargetApi
import android.graphics.Typeface
import android.os.Build
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.View
import android.view.animation.AnimationUtils
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.app.MyApplication
import com.stan.video.bittyvideo.base.BaseActivity
import com.stan.video.bittyvideo.utils.ViewAnimUtils
import kotlinx.android.synthetic.main.activity_search.*

/**
 * Created by Stan
 * on 2019/6/27.
 */
class SearchActivity:BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_search
    private var mTextTypeface: Typeface? = null
    init {

        mTextTypeface = Typeface.createFromAsset(MyApplication.context.assets,"fonts/FZLanTingHeiS-L-GB-Regular.TTF")
    }
    override fun initData() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            setUpEnterAnimation() // 入场动画
            setUpExitAnimation() // 退场动画
        }else{
            setUpView()
        }


    }



    override fun initView() {
        tv_title_tip.typeface  = mTextTypeface
        tv_hot_search_words.typeface = mTextTypeface
    }

    override fun start() {

    }
    private fun setUpExitAnimation() {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpEnterAnimation() {
        val transition = TransitionInflater.from(this)
                .inflateTransition(R.transition.arc_motion)
        window.sharedElementEnterTransition = transition
        transition.addListener(object : Transition.TransitionListener{
            override fun onTransitionEnd(transition: Transition) {
                transition.removeListener(this)
                animateRevealShow()
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

    /**
     * 展示动画
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun animateRevealShow() {
        ViewAnimUtils.animatRevelshow(this,rel_frame,fab_circle.width/2,R.color.backgroundColor,
                object : ViewAnimUtils.OnRevealAnimationListener{
                    override fun onRevealHide() {

                    }

                    override fun onRevealShow() {
                        setUpView()
                    }

                })
    }

    private fun setUpView() {
        val animation = AnimationUtils.loadAnimation(this,android.R.anim.fade_in)
        animation.duration = 300
        rel_container.animation = animation
        rel_container.visibility = View.VISIBLE
        //打开软件盘
        openKeyBord(et_search_view,applicationContext)
    }

}