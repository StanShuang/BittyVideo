package com.stan.video.bittyvideo.ui.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.base.BaseViewFragment
import com.stan.video.bittyvideo.databinding.LayoutRecycleviewBinding
import com.stan.video.bittyvideo.ext.showToast
import com.stan.video.bittyvideo.mvp.contract.CategoryContract
import com.stan.video.bittyvideo.mvp.model.bean.CategoryBean
import com.stan.video.bittyvideo.mvp.presenter.CategoryPresenter
import com.stan.video.bittyvideo.net.exception.ErrorStatus
import com.stan.video.bittyvideo.ui.adapter.CategoryAdapter
import com.stan.video.bittyvideo.utils.DisplayManager

/**
 * Created by Stan
 * on 2019/6/24.
 */
class CategoryFragment : BaseViewFragment(), CategoryContract.View {
    private lateinit var binding: LayoutRecycleviewBinding
    private val mPresenter by lazy { CategoryPresenter() }
    private var mTitle: String? = null
    private var itemLists = ArrayList<CategoryBean>()
    private val mAdapter by lazy {
        activity?.let {
            CategoryAdapter(
                it,
                itemLists,
                R.layout.item_category
            )
        }
    }

    override fun getLayoutView(): View {
        binding = LayoutRecycleviewBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        fun getInstance(title: String): CategoryFragment {
            val fragment = CategoryFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun lazyLoad() {
        mPresenter.getCategoryData()
    }

    override fun initView() {
        mPresenter.attachView(this)
        mLayoutStatusView = binding.multipleStatusView
        binding.mRecyclerView.run {
            layoutManager = GridLayoutManager(activity, 2)
            adapter = mAdapter
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val position = parent.getChildAdapterPosition(view)
                    val offset = DisplayManager.dip2px(2f)!!
                    outRect.set(
                        if (position % 2 == 0) 0 else offset,
                        offset,
                        if (position % 2 == 0) offset else 0,
                        offset
                    )

                }
            })
        }

    }

    override fun showLoading() {
        binding.multipleStatusView.showLoading()
    }

    override fun dismissLoading() {
        binding.multipleStatusView.showContent()
    }

    override fun showCategory(categoryList: ArrayList<CategoryBean>) {
        itemLists = categoryList
        mAdapter?.setCategoryData(categoryList)

    }

    override fun showError(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            binding.multipleStatusView.showNoNetwork()
        } else {
            binding.multipleStatusView.showError()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}