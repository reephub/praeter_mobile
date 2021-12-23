package com.reephub.praeter.ui.meetancients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.reephub.praeter.R
import com.reephub.praeter.data.remote.dto.AncientDto
import com.reephub.praeter.databinding.FragmentMeetAncientsBinding
import com.reephub.praeter.ui.base.BaseFragment
import com.reephub.praeter.ui.mainactivity.MainActivityViewModel
import timber.log.Timber

class MeetAncientsFragment : BaseFragment() {
    private var _viewBinding: FragmentMeetAncientsBinding? = null
    private val binding get() = _viewBinding!!

    private val mViewModel: MainActivityViewModel by activityViewModels()


    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentMeetAncientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.d("onViewCreated()")
        binding.toolbar.title = requireActivity().getString(R.string.title_meet_ancients)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

        initViewModelObservers()

        mViewModel.fetchAncients()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }


    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    private fun initViewModelObservers() {
        Timber.d("initViewModelObservers()")
        mViewModel.getAncients().observe(viewLifecycleOwner, {
            bindData(it)
        })
    }

    private fun bindData(items: List<AncientDto>) {
        Timber.d("bindData()")

        val adapter = AncientAdapter(items/*, this*/)

        val linearLayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        binding.rvAncients.layoutManager = linearLayoutManager

        binding.rvAncients.itemAnimator = DefaultItemAnimator()

        // To solve the return transition problem, we need to add this lines on the Source Fragment
        // where you initialise your recycler view.
        binding.rvAncients.apply {
            binding.rvAncients.adapter = adapter
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
    }


    override fun onConnected(isConnected: Boolean) {
        // Ignored
    }
}