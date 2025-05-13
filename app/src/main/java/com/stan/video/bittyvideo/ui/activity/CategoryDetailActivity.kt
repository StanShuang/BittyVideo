package com.stan.video.bittyvideo.ui.activity

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseViewActivity
import com.stan.video.bittyvideo.databinding.ActivityCategoryDetailBinding
import com.stan.video.bittyvideo.mvp.contract.CategoryDetailContract
import com.stan.video.bittyvideo.mvp.model.bean.CategoryBean
import com.stan.video.bittyvideo.mvp.model.bean.HomeBean
import com.stan.video.bittyvideo.mvp.presenter.CategoryDetailPresenter
import com.stan.video.bittyvideo.ui.adapter.CategoryDetailAdapter
import com.stan.video.bittyvideo.utils.Constant
import com.stan.video.bittyvideo.utils.StatusBarUtil

/**
 * Created by Stan
 * on 2019/6/26.
 */
class CategoryDetailActivity : BaseViewActivity(), CategoryDetailContract.View {
    private lateinit var binding: ActivityCategoryDetailBinding
    private var categoryData: CategoryBean? = null
    private var itemList = ArrayList<HomeBean.Issue.Item>()
    private val mPresenter by lazy { CategoryDetailPresenter() }
    private val mAdapter by lazy {
        CategoryDetailAdapter(
            this,
            itemList,
            R.layout.item_category_detail
        )
    }

    override fun layoutView(): View {
        binding = ActivityCategoryDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    private var loadingMore: Boolean = false

    init {
        mPresenter.attachView(this)
    }

    override fun initData() {
        categoryData = intent.getSerializableExtra(Constant.BUNDLE_CATEGORY_DATA) as CategoryBean?
    }

    override fun initView() {
        binding.toolbar.run {
            setSupportActionBar(this)
            setNavigationOnClickListener { finish() }
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        setTextView()
        binding.mRecyclerView.run {
            layoutManager = LinearLayoutManager(this@CategoryDetailActivity)
            adapter = mAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val itemCount = binding.mRecyclerView.layoutManager!!.itemCount
                    val lastVisibleItem =
                        (binding.mRecyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if (!loadingMore && lastVisibleItem == (itemCount - 1)) {
                        loadingMore = true
                        mPresenter.loadMoreData()
                    }
                }
            })
        }
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, binding.toolbar)

    }

    private fun setTextView() {
        Glide.with(this)
            .load(categoryData?.headerImage)
            .placeholder(R.color.color_darker_gray)
            .into(binding.imageView)
        binding.tvCategoryDesc.text = "#${categoryData?.description}#"
        binding.collapsingToolbarLayout.run {
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
        binding.multipleStatusView.showError()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}