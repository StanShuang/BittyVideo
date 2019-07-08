package com.stan.video.bittyvideo.ui.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.header.MaterialHeader
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseFragment
import com.stan.video.bittyvideo.ext.showToast
import com.stan.video.bittyvideo.mvp.contract.HomeContract
import com.stan.video.bittyvideo.mvp.model.bean.HomeBean
import com.stan.video.bittyvideo.mvp.presenter.HomePresenter
import com.stan.video.bittyvideo.net.exception.ErrorStatus
import com.stan.video.bittyvideo.ui.activity.SearchActivity
import com.stan.video.bittyvideo.ui.adapter.HomeAdapter
import com.stan.video.bittyvideo.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Stan
 * on 2019/6/6.
 */
@Suppress("DEPRECATION")
class HomeFragment: BaseFragment() ,HomeContract.View{
    private var mTitle: String? = null
    override fun getLayoutId(): Int = R.layout.fragment_home
    private val mPresenter: HomePresenter by lazy { HomePresenter() }
    //是否是在下拉刷新
    private var isRefresh = false
    private var loadingMore = false
    private var num: Int = 1
//    private var data = mutableListOf<HomeBean.Issue.Item>()
    private var mHomeAdapter: HomeAdapter? = null
    private var mMaterialHeader: MaterialHeader? = null
    companion object {
        fun getInstance(title: String): HomeFragment{
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }
    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }


    private val simpleDateFormat by lazy {
        SimpleDateFormat("- MMM. dd, 'Brunch' -", Locale.ENGLISH)
    }
    override fun lazyLoad() {
        mPresenter.requestHomeData(num)
    }

    override fun initView() {
        mPresenter.attachView(this)
        //内容跟随偏移
        mRefreshLayout.setEnableHeaderTranslationContent(true)
        mRefreshLayout.setOnRefreshListener {
            isRefresh = true
            mPresenter.requestHomeData(num)
        }
        mMaterialHeader = mRefreshLayout.refreshHeader as MaterialHeader?
        //打开下拉刷新区域块背景:
        mMaterialHeader?.setShowBezierWave(true)
        //设置下拉刷新主题颜色
        mRefreshLayout.setPrimaryColorsId(R.color.color_light_black, R.color.color_title_bg)

        iv_search.setOnClickListener {
            openSearchActivity()
        }
        mLayoutStatusView = multipleStatusView
        mRecyclerView.addOnScrollListener(scrollListener)
        //状态栏透明和间距处理
        activity?.run {
            StatusBarUtil.darkMode(this)
            StatusBarUtil.setPaddingSmart(this, toolbar)
        }
    }

    private fun openSearchActivity() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val pair = activity?.let { ActivityOptionsCompat.makeSceneTransitionAnimation(it,iv_search,iv_search.transitionName) }
            startActivity(Intent(activity,SearchActivity :: class.java),pair?.toBundle())
        }else{
            startActivity(Intent(activity,SearchActivity :: class.java))
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == RecyclerView.SCROLL_STATE_IDLE){
                //可见的item数量
                val childcount = recyclerView.childCount
                //整体的数量
                val itemcount = recyclerView.layoutManager.itemCount
                //可见的第一个item的位置
                val fistVisbleItem =(recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if(fistVisbleItem + childcount == itemcount){
                    if(!loadingMore){
                        loadingMore = true
                        mPresenter.loadMoreData()
                    }
                }

            }
        }
        //RecyclerView滚动的时候调用
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val currentVisibleItmePosition = linearLayoutManager.findFirstVisibleItemPosition()
            if(currentVisibleItmePosition == 0){
                toolbar.setBackgroundColor(resources.getColor(R.color.color_translucent))
                iv_search.setImageResource(R.mipmap.ic_action_search_white)
                tv_header_title.text = ""
            }else{
                if(mHomeAdapter?.mData!!.size >1 ){
                    toolbar.setBackgroundColor(resources.getColor(R.color.color_title_bg))
                    iv_search.setImageResource(R.mipmap.ic_action_search_black)
                    val itemList = mHomeAdapter!!.mData
                    val item = itemList[currentVisibleItmePosition + mHomeAdapter!!.bannerItemSize -1]
                    if(item.type == "textHeader"){
                        tv_header_title.text = item.data?.text
                    }else{
                        tv_header_title.text = simpleDateFormat.format(item.data?.date)
                    }

                }

            }

        }

    }
    /**
     * 下拉刷新时不展示loading
     */
    override fun showLoading() {
        if(!isRefresh){
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }

    }

    override fun dismissLoading() {
      mRefreshLayout.finishRefresh()
    }

    override fun setHomeData(homeBean: HomeBean) {
        mLayoutStatusView?.showContent()
        Logger.d(homeBean)
        // Adapter
        mHomeAdapter = activity?.let { HomeAdapter(it, homeBean.issueList[0].itemList) }
        //设置 banner 大小
        mHomeAdapter?.setBannerSize(homeBean.issueList[0].count)

        mRecyclerView.adapter = mHomeAdapter
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
    }

    override fun setMoreData(itemList: ArrayList<HomeBean.Issue.Item>) {
        loadingMore = false
        mHomeAdapter?.addItemData(itemList)
    }

    override fun showError(msg: String, errorCode: Int) {
        showToast(msg)
        if(errorCode == ErrorStatus.NETWORK_ERROR){
            mLayoutStatusView?.showNoNetwork()
        }else{
            mLayoutStatusView?.showError()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}