package com.reephub.praeter.ui.classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.reephub.praeter.databinding.FragmentClassesBinding
import com.reephub.praeter.ui.base.BaseFragment
import com.reephub.praeter.ui.mainactivity.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassesFragment : BaseFragment() {

    private var _viewBinding: FragmentClassesBinding? = null
    private val binding get() = _viewBinding!!

    private val mViewModel: MainActivityViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentClassesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.fetchOrders()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    override fun onConnected(isConnected: Boolean) {
        // Ignored
    }
}