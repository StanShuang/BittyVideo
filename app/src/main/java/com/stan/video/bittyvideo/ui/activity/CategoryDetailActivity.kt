package com.stan.video.bittyvideo.ui.activity

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseActivity
import com.stan.video.bittyvideo.glide.GlideApp
import com.stan.video.bittyvideo.mvp.contract.CategoryDetailContract
import com.stan.video.bittyvideo.mvp.model.bean.CategoryBean
import com.stan.video.bittyvideo.mvp.model.bean.HomeBean
import com.stan.video.bittyvideo.mvp.presenter.CategoryDetailPresenter
import com.stan.video.bittyvideo.ui.adapter.CategoryDetailAdapter
import com.stan.video.bittyvideo.utils.Constant
import com.stan.video.bittyvideo.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_category_detail.*

/**
 * Created by Stan
 * on 2019/6/26.
 */
class CategoryDetailActivity: BaseActivity() , CategoryDetailContract.View{
    private var categoryData: CategoryBean? = null
    private var itemList = ArrayList<HomeBean.Issue.Item>()
    private val mPresenter by lazy { CategoryDetailPresenter() }
    private val mAdapter by lazy { CategoryDetailAdapter(this,itemList,R.layout.item_category_detail) }
    override fun layoutId(): Int = R.layout.activity_category_detail
    private var loadingMore: Boolean = false
    init {
        mPresenter.attachView(this)
    }

    override fun initData() {
        categoryData = intent.getSerializableExtra(Constant.BUNDLE_CATEGORY_DATA) as CategoryBean?
    }

    override fun initView() {
        toolbar.run {
            setSupportActionBar(this)
            setNavigationOnClickListener { finish() }
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        setTextView()
        mRecyclerView.run {
            layoutManager = LinearLayoutManager(this@CategoryDetailActivity)
            adapter = mAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val itemCount = mRecyclerView.layoutManager.itemCount
                    val lastVisibleItem = (mRecyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if(!loadingMore && lastVisibleItem == (itemCount -1)){
                        loadingMore = true
                        mPresenter.loadMoreData()
                    }
                }
            })
        }
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this,toolbar)

    }

    private fun setTextView() {
        GlideApp.with(this)
                .load(categoryData?.headerImage)
                .placeholder(R.color.color_darker_gray)
                .into(imageView)
        tv_category_desc.text = "#${categoryData?.description}#"
        collapsing_toolbar_layout.run {
            title = categoryData?.name
            //设置还没收缩时状态下字体颜色
            setExpandedTitleColor(Color.WHITE)
            //设置收缩后Toolbar上字体的颜色
            setCollapsedTitleTextColor(Color.BLACK)
        }

    }

    override fun start() {
        categoryData?.id?.let {
            mPresenter.getCategoryDetailList(it)
        }
    }
    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    override fun setCateDetailList(itemList: ArrayList<HomeBean.Issue.Item>) {
        loadingMore = false
        mAdapter.addData(itemList)
    }

    override fun showError(errorMsg: String) {
        multipleStatusView.showError()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}