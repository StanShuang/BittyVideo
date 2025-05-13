package com.stan.video.bittyvideo.mvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stan.video.bittyvideo.databinding.FragmentRefreshLayoutBinding

/**
 *@Author Stan
 *@Description
 *@Date 2025/4/11 15:49
 */
class FollowFragment : Fragment() {
    private var _binding: FragmentRefreshLayoutBinding? = null
    private val binding: FragmentRefreshLayoutBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRefreshLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(): Fragment = FollowFragment()
    }
}