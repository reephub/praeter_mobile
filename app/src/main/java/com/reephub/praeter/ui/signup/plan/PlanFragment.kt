package com.reephub.praeter.ui.signup.plan

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.reephub.praeter.R
import com.reephub.praeter.databinding.FragmentPlanBinding
import com.reephub.praeter.ui.signup.NextViewPagerClickListener
import com.reephub.praeter.ui.signup.SignUpViewModel
import timber.log.Timber

class PlanFragment : Fragment(),
    View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private var _viewBinding: FragmentPlanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val mViewModel: SignUpViewModel by activityViewModels()

    private lateinit var mListener: NextViewPagerClickListener

    private var hasPlanSelected: Boolean = false


    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as NextViewPagerClickListener
        } catch (exception: ClassCastException) {
            throw ClassCastException(
                "${activity.toString()} must implement ${NextViewPagerClickListener::class.java.simpleName}"
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
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
    private fun setListeners() {
        binding.rbFreePlan.setOnCheckedChangeListener(this)
        binding.rbPremiumPlan.setOnCheckedChangeListener(this)
        binding.btnContinue.setOnClickListener(this)
    }

    private fun changeButtonState(isEnable: Boolean) {
        Timber.d("changeButtonState() to $isEnable")

        if (!hasPlanSelected) {
            binding.btnContinue.isEnabled = isEnable

            binding.btnContinue.backgroundTintList =
                ColorStateList.valueOf(
                    if (!isEnable) ContextCompat.getColor(
                        requireActivity(),
                        R.color.transparent
                    ) else ContextCompat.getColor(
                        requireActivity(),
                        R.color.purple_500
                    )
                )

            binding.btnContinue.setTextColor(
                if (!isEnable) ContextCompat.getColor(
                    requireActivity(),
                    R.color.jumbo
                ) else ContextCompat.getColor(
                    requireActivity(),
                    R.color.white
                )
            )
        }
    }

    /////////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////////
    override fun onClick(view: View?) {

        if (binding.rbFreePlan.isChecked) {
            mViewModel.setUserPremium(false)
            mListener.onLastViewPagerClicked()
        }

        if (binding.rbPremiumPlan.isChecked) {
            mViewModel.setUserPremium(true)
            mListener.onNextViewPagerClicked()
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        Timber.d("onCheckedChanged()")

        if (hasPlanSelected)
            return
        else {
            if (!hasPlanSelected)
                changeButtonState(isChecked)

            hasPlanSelected = true
        }
    }

    companion object {
        val TAG: String = PlanFragment::class.java.simpleName

        fun newInstance(): PlanFragment {
            val args = Bundle()
            val fragment = PlanFragment()
            fragment.arguments = args
            return fragment
        }
    }

}