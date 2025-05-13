package com.stan.video.bittyvideo.ui.activity

import android.graphics.Typeface
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
import com.stan.video.bittyvideo.base.BaseViewActivity
import com.stan.video.bittyvideo.databinding.ActivitySearchBinding
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

/**
 * Created by Stan
 * on 2019/6/27.
 */
class SearchActivity : BaseViewActivity(), SearchContract.View {
    private lateinit var binding: ActivitySearchBinding
    private val mPresenter by lazy { SearchPresenter() }
    override fun layoutView(): View {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mTextTypeface: Typeface? = null
    private var mAdapter: HotKeywordsAdapter? = null
    private var itemList = ArrayList<HomeBean.Issue.Item>()
    private val mResultAdapter by lazy {
        CategoryDetailAdapter(
            this,
            itemList,
            R.layout.item_category_detail
        )
    }
    private var keyWords: String? = null
    private var loadingMore = false

    init {
        mPresenter.attachView(this)
        mTextTypeface = Typeface.createFromAsset(
            MyApplication.context.assets,
            "fonts/FZLanTingHeiS-L-GB-Regular.TTF"
        )
    }

    override fun initData() {
        setUpEnterAnimation() // 入场动画
        setUpExitAnimation() // 退场动画


    }


    override fun initView() {
        binding.tvTitleTip.typeface = mTextTypeface
        binding.tvHotSearchWords.typeface = mTextTypeface

        binding.tvCancel.setOnClickListener {
            onBackPressed()
        }
        binding.mRecyclerViewResult.run {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = mResultAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val itemCount = binding.mRecyclerViewResult.layoutManager?.itemCount
                    val lastVisibleItem =
                        (binding.mRecyclerViewResult.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if (!loadingMore && lastVisibleItem == itemCount!! - 1) {
                        loadingMore = true
                        mPresenter.loadMoreData()
                    }
                }
            })
        }
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, binding.toolbar)
        mLayoutStatusView = binding.multipleStatusView
        binding.etSearchView.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    closeSoftKeyboard()
                    keyWords = binding.etSearchView.text.toString().trim()
                    if (keyWords.isNullOrEmpty()) {
                        showToast("请输入你感兴趣的关键词")
                    } else {
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

    private fun setUpExitAnimation() {
        val fade = Fade()
        window.reenterTransition = fade
        fade.duration = 300

    }

    private fun setUpEnterAnimation() {
        val transition = TransitionInflater.from(this)
            .inflateTransition(R.transition.arc_motion)
        window.sharedElementEnterTransition = transition
        transition.addListener(object : Transition.TransitionListener {
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
    private fun animateRevealShow() {
        ViewAnimUtils.animatRevelshow(
            this, binding.relFrame, binding.fabCircle.width / 2, R.color.backgroundColor,
            object : ViewAnimUtils.OnRevealAnimationListener {
                override fun onRevealHide() {

                }

                override fun onRevealShow() {
                    setUpView()
                }

            })
    }

    private fun setUpView() {
        val animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        animation.duration = 300
        binding.relContainer.animation = animation
        binding.relContainer.visibility = View.VISIBLE
        //打开软件盘
        openKeyBord(binding.etSearchView, applicationContext)
    }

    override fun onBackPressed() {
        ViewAnimUtils.animateRevealHide(
            this, binding.relFrame,
            binding.fabCircle.width / 2, R.color.backgroundColor,
            object : ViewAnimUtils.OnRevealAnimationListener {
                override fun onRevealHide() {
                    defaultBackPressed()
                }

                override fun onRevealShow() {

                }

            })

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
        mAdapter = HotKeywordsAdapter(this, string, R.layout.item_flow_text)
        val flexBoxLayoutManager = FlexboxLayoutManager(this)
        flexBoxLayoutManager.flexWrap = FlexWrap.WRAP // 按正常方向换行
        flexBoxLayoutManager.flexDirection = FlexDirection.ROW //主轴在水平方向，起点在左端
        flexBoxLayoutManager.alignItems = AlignItems.CENTER  //定义项目在副轴轴上如何对齐
        flexBoxLayoutManager.justifyContent = JustifyContent.FLEX_START //多个轴对齐方式
        binding.mRecyclerViewHot.layoutManager = flexBoxLayoutManager
        binding.mRecyclerViewHot.adapter = mAdapter
        mAdapter?.setOnTagItemClickListener {
            closeSoftKeyboard()
            keyWords = it
            mPresenter.querySearchData(it)
        }
    }

    private fun setHotWordView() {
        binding.layoutHotWords.visibility = View.VISIBLE
        binding.layoutContentResult.visibility = View.GONE
    }

    override fun setSearchResult(issue: HomeBean.Issue) {
        loadingMore = false
        hideHotWordView()
        binding.tvSearchCount.visibility = View.VISIBLE
        binding.tvSearchCount.text =
            String.format(resources.getString(R.string.search_result_count), keyWords, issue.total)
        itemList = issue.itemList
        mResultAdapter.addData(issue.itemList)
    }

    private fun hideHotWordView() {
        binding.layoutHotWords.visibility = View.GONE
        binding.layoutContentResult.visibility = View.VISIBLE
    }

    override fun setEmptyView() {
        showToast("抱歉，没有找到相匹配的内容")
        hideHotWordView()
        binding.tvSearchCount.visibility = View.GONE
        mLayoutStatusView?.showEmpty()
    }

    override fun showError(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            mLayoutStatusView?.showNoNetwork()
        } else {
            mLayoutStatusView?.showError()
        }
    }

    /**
     * 关闭软键盘
     */
    override fun closeSoftKeyboard() {
        closeKeyBord(binding.etSearchView, applicationContext)
    }

}