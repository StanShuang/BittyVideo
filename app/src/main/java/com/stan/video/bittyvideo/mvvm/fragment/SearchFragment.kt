package com.stan.video.bittyvideo.mvvm.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.stan.video.bittyvideo.R
import com.stan.video.bittyvideo.databinding.FrmentSearchBinding
import com.stan.video.bittyvideo.mvvm.adapter.HotSearchAdapter
import com.stan.video.bittyvideo.mvvm.viewmodel.factory.InjectorUtil
import com.stan.video.bittyvideo.mvvm.viewmodel.SearchViewModel
import com.stan.video.bittyvideo.utils.StatusBarUtil
import com.stan.video.bittyvideo.view.setDrawable
import com.stan.video.bittyvideo.view.showToast
import com.stan.video.bittyvideo.view.visibleAlphaAnimation

/**
 *@Author Stan
 *@Description
 *@Date 2025/4/9 15:46
 */
class SearchFragment : Fragment() {
    private var _binding: FrmentSearchBinding? = null
    private val binding: FrmentSearchBinding
        get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            InjectorUtil.getSearchViewModelFactory()
        )[SearchViewModel::class.java]
    }

    private lateinit var adapter: HotSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FrmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StatusBarUtil.darkMode(requireActivity())
        StatusBarUtil.setPaddingSmart(requireActivity(), binding.llSearch)
        binding.llSearch.visibleAlphaAnimation(500)
        binding.etQuery.setDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.ic_search_gray_17dp
            ), 14f, 14f
        )
        binding.etQuery.setOnEditorActionListener(EditorActionListener(requireActivity(), binding))
        binding.tvCancel.setOnClickListener {
            hideSoftKeyboard()
            removeFragment(requireActivity(), this)
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.layoutManager = layoutManager
        adapter = HotSearchAdapter(viewModel.dataList)
        binding.recyclerView.adapter = adapter
        viewModel.onRefresh()
        observe()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observe() {
        viewModel.dataListLiveData.observe(viewLifecycleOwner, Observer { result ->
            binding.etQuery.showSoftKeyboard()
            val response = result.getOrNull()
            if (response == null) {
                result.exceptionOrNull()?.printStackTrace()
                return@Observer
            }
            if (response.isEmpty()) {
                return@Observer
            }
            viewModel.dataList.clear()
            viewModel.dataList.addAll(response)
            adapter.notifyDataSetChanged()
        })
    }

    class EditorActionListener(activity: Context, val binding: FrmentSearchBinding) :
        TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.etQuery.text.toString().isEmpty()) {
                    R.string.input_keywords_tips.showToast()
                    return false
                }
                R.string.currently_not_supported.showToast()
                return true
            }
            return true
        }

    }

    private fun hideSoftKeyboard() {
        requireActivity().currentFocus?.run {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    private fun View.showSoftKeyboard() {
        this.isFocusable = true
        this.isFocusableInTouchMode = true
        this.requestFocus()
        val manager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.showSoftInput(this, 0)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            AnimationUtils.loadAnimation(requireActivity(), R.anim.anl_push_up_in)
        } else {
            AnimationUtils.loadAnimation(requireActivity(), R.anim.anl_push_top_out)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        hideSoftKeyboard()
        _binding = null
    }

    companion object {
        /**
         * 切换Fragment，会加入回退栈。
         */
        fun switchFragment(activity: Activity) {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, SearchFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        /**
         * 先移除Fragment，并将Fragment从堆栈弹出。
         */
        fun removeFragment(activity: Activity, fragment: Fragment) {
            (activity as AppCompatActivity).supportFragmentManager.run {
                beginTransaction().remove(fragment).commitAllowingStateLoss()
                popBackStack()
            }
        }
    }


}