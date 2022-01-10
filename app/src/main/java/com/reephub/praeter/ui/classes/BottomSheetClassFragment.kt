package com.reephub.praeter.ui.classes

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.reephub.praeter.R
import com.reephub.praeter.core.utils.PraeterNetworkManagerNewAPI
import com.reephub.praeter.core.utils.UIManager
import com.reephub.praeter.data.remote.dto.ClassesDto
import com.reephub.praeter.databinding.FragmentBottomClassBinding
import jp.wasabeef.glide.transformations.BlurTransformation
import timber.log.Timber

class BottomSheetClassFragment : BottomSheetDialogFragment(),
    View.OnClickListener {

    private var _viewBinding: FragmentBottomClassBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private var isConnected: Boolean = false

    private var storedClass: ClassesDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")
        assert(arguments != null)

        if (null == arguments?.getParcelable(CLASS_ITEM_BUNDLE)) {
            Timber.e("Bundle with key : CLASS_ITEM_BUNDLE, is null ")
            return
        }

        storedClass = arguments?.getParcelable(CLASS_ITEM_BUNDLE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentBottomClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.i("onViewCreated()")

        initViewModelObservers()
        setListeners()

        if (null != storedClass)
            setViews(storedClass!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _viewBinding = null
    }

    private fun initViewModelObservers() {
        PraeterNetworkManagerNewAPI
            .getInstance(requireContext())
            .getConnectionState()
            .observe(
                viewLifecycleOwner,
                {
                    isConnected = it

                    UIManager.showConnectionStatusInSnackBar(
                        requireActivity(),
                        isConnected
                    )
                })
    }

    private fun setListeners() {
        binding.closeBottomSheetBtn.setOnClickListener(this)
        binding.btnBook.setOnClickListener(this)
    }

    private fun book() {
        Timber.i("onReserveButtonClicked()")
        if (!isConnected) {
            val errorMessage = "Not connected to the internet. Check your internet connection."
            Timber.e(errorMessage)
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            return
        }
        val message = "You've successfully reserved the class " + storedClass?.name
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun close() {
        Timber.i("onCloseButtonClicked()")
        dismiss()
    }

    @SuppressLint("SetTextI18n")
    private fun setViews(classDto: ClassesDto) {
        binding.mClass = classDto

        Glide.with(this)
            .load(R.drawable.visa_icon)
            .transform(
                BlurTransformation(75),
                RoundedCorners(48)
            )
            .into(binding.ivClassThumbBlurred)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Timber.e("onDismiss()")
    }

    override fun onClick(view: View?) {

        if (view?.id == R.id.btn_book) {
            book()
        }
        if (view?.id == R.id.close_bottom_sheet_btn) {
            close()
        }
    }

    companion object {
        const val TAG = "BottomSheetClassFragment"

        const val CLASS_ITEM_BUNDLE = "CLASS_ITEM_BUNDLE"

        fun newInstance(classDto: ClassesDto): BottomSheetClassFragment {
            val args = Bundle()
            args.putParcelable(CLASS_ITEM_BUNDLE, classDto)
            val fragment = BottomSheetClassFragment()
            fragment.arguments = args
            return fragment
        }
    }
}