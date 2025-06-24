package com.stan.video.bittyvideo.mvvm.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.databinding.FragmentRefreshLayoutBinding
import com.stan.video.bittyvideo.mvvm.adapter.CommendAdapter
import com.stan.video.bittyvideo.mvvm.adapter.FooterAdapter
import com.stan.video.bittyvideo.mvvm.adapter.decoration.ItemDecoration
import com.stan.video.bittyvideo.mvvm.viewmodel.CommendViewModel
import com.stan.video.bittyvideo.mvvm.viewmodel.factory.InjectorUtil
import com.stan.video.bittyvideo.utils.GlobalUtil
import com.stan.video.bittyvideo.utils.ResponseHandler
import com.stan.video.bittyvideo.utils.showToast
import com.stan.video.bittyvideo.view.gone
import com.stan.video.bittyvideo.view.visible
import kotlinx.coroutines.launch

/**
 *@Author Stan
 *@Description
 *@Date 2025/4/11 15:48
 */
class CommendFragment : Fragment() {
    private var _binding: FragmentRefreshLayoutBinding? = null
    private val binding: FragmentRefreshLayoutBinding
        get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getCommunityCommendViewModelFactory()
        )[CommendViewModel::class.java]
    }

    private lateinit var adapter: CommendAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRefreshLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = CommendAdapter(this)
        val mainLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mainLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        binding.recyclerView.layoutManager = mainLayoutManager
        binding.recyclerView.adapter = adapter.withLoadStateFooter(FooterAdapter(adapter::retry))
        binding.recyclerView.addItemDecoration(ItemDecoration())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.itemAnimator = null
        binding.refreshLayout.setOnRefreshListener { adapter.refresh() }
        addLoadStateListener()

        lifecycleScope.launch {
            viewModel.getPagingData().collect {
                adapter.submitData(it)
            }
        }
    }

    private fun addLoadStateListener() {
        adapter.addLoadStateListener {
            //列表初始加载或手动下拉刷新
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    loadFinished()
                    if (it.source.append.endOfPaginationReached) {
                        binding.refreshLayout.setEnableLoadmore(true)
                        binding.refreshLayout.finishLoadmore()
                    } else {
                        binding.refreshLayout.setEnableLoadmore(false)
                    }
                }

                is LoadState.Loading -> {
                    if (binding.refreshLayout.state != RefreshState.Refreshing) {
                        startLoading()
                    }
                }

                is LoadState.Error -> {
                    val state = it.refresh as LoadState.Error
                    loadFail(ResponseHandler.getFailureTips(state.error))
                }
            }
            //用户滚动到列表底部时加载更多数据
            when (it.append) {
                is LoadState.NotLoading -> {
                    Log.d(
                        "Stan--FollowFragment",
                        "endOfPaginationReached=${it.source.append.endOfPaginationReached}"
                    )
                    if (it.source.append.endOfPaginationReached) {
                        binding.refreshLayout.setEnableLoadmore(false)
                        binding.refreshLayout.finishLoadmore()
                    } else {
                        binding.refreshLayout.setEnableLoadmore(true)
                    }
                }

                is LoadState.Loading -> {

                }

                is LoadState.Error -> {
                    val state = it.append as LoadState.Error
                    ResponseHandler.getFailureTips(state.error).showToast()
                }
            }
        }
    }

    private fun loadFinished() {
        binding.refreshLayout.visible()
        binding.refreshLayout.finishRefresh()
    }

    private fun startLoading() {
        binding.refreshLayout.gone()
    }

    private fun loadFail(msg: String?) {
        binding.refreshLayout.finishRefresh()
        showLoadErrorView(msg ?: GlobalUtil.getString(R.string.unknown_error)) {
            startLoading()
            adapter.refresh()
        }

    }

    private fun showLoadErrorView(tip: String, block: View.() -> Unit) {

    }

    companion object {
        fun newInstance(): Fragment = CommendFragment()
    }
}