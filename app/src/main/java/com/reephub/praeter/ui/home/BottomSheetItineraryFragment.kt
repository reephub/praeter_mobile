package com.reephub.praeter.ui.home

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.libraries.places.api.model.Place
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.reephub.praeter.databinding.FragmentBottomItineraryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetItineraryFragment : BottomSheetDialogFragment() {

    override fun getTheme(): Int {
        return android.R.style.Theme_Translucent
    }

    private var _viewBinding: FragmentBottomItineraryBinding? = null
    private val binding get() = _viewBinding!!

    private val mViewModel: HomeLocationViewModel by viewModels()

    private var location: Location? = null
    private var currentPlace: Place? = null
    private var targetPlace: Place? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = this.arguments

        if (null != extras) {
            currentPlace = extras.getParcelable(EXTRA_CURRENT_PLACE)
            targetPlace = extras.getParcelable(EXTRA_TARGET_PLACE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentBottomItineraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCloseBottomSheet.setOnClickListener {
            this.dismissAllowingStateLoss()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModelsObservers()

        if (null != currentPlace && null != targetPlace) {
            binding.currentPlace = currentPlace
            binding.targetPlace = targetPlace

            /*location?.let {
                mViewModel.getAddressFromLocation(requireContext(), it)
            }*/
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _viewBinding
    }

    private fun initViewModelsObservers() {
        /*mViewModel.getAddress().observe(requireActivity(), {
            Timber.e("getAddress().observe - ${it.toString()}")
            binding.currentAddress = it
        })*/
    }

    companion object {
        const val TAG = "BottomSheetLocationFragment"

        private const val EXTRA_CURRENT_PLACE = "extra_current_place"
        private const val EXTRA_TARGET_PLACE = "extra_target_place"

        fun newInstance(currentPlace: Place, targetLocation: Place): BottomSheetItineraryFragment {
            val args = Bundle()
            args.putParcelable(EXTRA_CURRENT_PLACE, currentPlace)
            args.putParcelable(EXTRA_TARGET_PLACE, targetLocation)

            val fragment = BottomSheetItineraryFragment()
            fragment.arguments = args
            return fragment
        }
    }
}