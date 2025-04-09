package com.stan.video.bittyvideo.ui.activity

import android.annotation.TargetApi
import android.graphics.Typeface
import android.os.Build
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.*
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.app.MyApplication
import com.stan.video.bittyvideo.base.BaseActivity
import com.stan.video.bittyvideo.ext.showToast
import com.stan.video.bittyvideo.mvp.contract.SearchContract
import com.stan.video.bittyvideo.mvp.model.bean.HomeBean
import com.stan.video.bittyvideo.mvp.presenter.SearchPresenter
import com.stan.video.bittyvideo.net.exception.ErrorStatus
import com.stan.video.bittyvideo.ui.adapter.CategoryDetailAdapter
import com.stan.video.bittyvideo.ui.adapter.HotKeywordsAdapter
import com.stan.video.bittyvideo.utils.CleanLeakUtils
import com.stan.video.bittyvideo.utils.StatusBarUtil
import com.stan.video.bittyvideo.utils.ViewAnimUtils
import kotlinx.android.synthetic.main.activity_search.*

/**
 * Created by Stan
 * on 2019/6/27.
 */
class SearchActivity:BaseActivity() ,SearchContract.View{
    private val mPresenter by lazy { SearchPresenter() }
    override fun layoutId(): Int = R.layout.activity_search
    private var mTextTypeface: Typeface? = null
    private var mAdapter: HotKeywordsAdapter? = null
    private var itemList = ArrayList<HomeBean.Issue.Item>()
    private val mResultAdapter by lazy { CategoryDetailAdapter(this,itemList,R.layout.item_category_detail) }
    private var keyWords: String? = null
    private var loadingMore =  false
    init {
        mPresenter.attachView(this)
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

        tv_cancel.setOnClickListener{
            onBackPressed()
        }
        mRecyclerView_result.run {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = mResultAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val itemCount = mRecyclerView_result.layoutManager?.itemCount
                    val lastVisibleItem = (mRecyclerView_result.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if(!loadingMore && lastVisibleItem == itemCount!! - 1){
                        loadingMore = true
                        mPresenter.loadMoreData()
                    }
                }
            })
        }
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this,toolbar)
        mLayoutStatusView = multipleStatusView
        et_search_view.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    closeSoftKeyboard()
                    keyWords = et_search_view.text.toString().trim()
                    if(keyWords.isNullOrEmpty()){
                        showToast("请输入你感兴趣的关键词")
                    }else{
                        mPresenter.querySearchData(keyWords!!)
                    }
                }
                return false
            }

        })
    }

    override fun start() {
        mPresenter.requestHotWordData()

    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpExitAnimation() {
        val fade = Fade()
        window.reenterTransition = fade
        fade.duration = 300

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

    override fun onBackPressed() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ViewAnimUtils.animateRevealHide(this,rel_frame,
                    fab_circle.width/2,R.color.backgroundColor,
                    object : ViewAnimUtils.OnRevealAnimationListener{
                        override fun onRevealHide() {
                            defaultBackPressed()
                        }

                        override fun onRevealShow() {

                        }

                    })

        }else{
            defaultBackPressed()
        }
    }
    //默认回退
    private fun defaultBackPressed() {
        closeSoftKeyboard()
        super.onBackPressed()
    }

    override fun onDestroy() {
        CleanLeakUtils.fixInputMethodManagerLeak(this)
        super.onDestroy()
        mPresenter.detachView()
        mTextTypeface = null

    }
    override fun showLoading() {
       mLayoutStatusView?.showLoading()
    }

    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
    }
    //设置热门搜索词页面
    override fun setHotWordData(string: ArrayList<String>) {
        setHotWordView()
        mAdapter = HotKeywordsAdapter(this,string,R.layout.item_flow_text)
        val flexBoxLayoutManager = FlexboxLayoutManager(this)
        flexBoxLayoutManager.flexWrap = FlexWrap.WRAP // 按正常方向换行
        flexBoxLayoutManager.flexDirection = FlexDirection.ROW //主轴在水平方向，起点在左端
        flexBoxLayoutManager.alignItems =AlignItems.CENTER  //定义项目在副轴轴上如何对齐
        flexBoxLayoutManager.justifyContent = JustifyContent.FLEX_START //多个轴对齐方式
        mRecyclerView_hot.layoutManager= flexBoxLayoutManager
        mRecyclerView_hot.adapter = mAdapter
        mAdapter?.setOnTagItemClickListener {
            closeSoftKeyboard()
            keyWords = it
            mPresenter.querySearchData(it)
        }
    }

    private fun setHotWordView() {
        layout_hot_words.visibility = View.VISIBLE
        layout_content_result.visibility = View.GONE
    }

    override fun setSearchResult(issue: HomeBean.Issue) {
        loadingMore = false
        hideHotWordView()
        tv_search_count.visibility = View.VISIBLE
        tv_search_count.text = String.format(resources.getString(R.string.search_result_count),keyWords,issue.total)
        itemList = issue.itemList
        mResultAdapter.addData(issue.itemList)
    }

    private fun hideHotWordView() {
        layout_hot_words.visibility = View.GONE
        layout_content_result.visibility = View.VISIBLE
    }

    override fun setEmptyView() {
        showToast("抱歉，没有找到相匹配的内容")
        hideHotWordView()
        tv_search_count.visibility = View.GONE
        mLayoutStatusView?.showEmpty()
    }

    override fun showError(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if(errorCode == ErrorStatus.NETWORK_ERROR){
            mLayoutStatusView?.showNoNetwork()
        }else{
            mLayoutStatusView?.showError()
        }
    }
    /**
     * 关闭软键盘
     */
    override fun closeSoftKeyboard() {
        closeKeyBord(et_search_view, applicationContext)
    }

}