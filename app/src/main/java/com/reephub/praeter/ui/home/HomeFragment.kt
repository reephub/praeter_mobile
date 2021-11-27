package com.reephub.praeter.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.reephub.praeter.databinding.FragmentHomeBinding
import com.reephub.praeter.ui.base.BaseFragment

class HomeFragment : BaseFragment() {
    private var _viewBinding: FragmentHomeBinding? = null
    private val binding get() = _viewBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    override fun onConnected(isConnected: Boolean) {
        // Ignored
    }
}