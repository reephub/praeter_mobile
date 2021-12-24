package com.reephub.praeter.ui.signup.plan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.reephub.praeter.databinding.FragmentPlanBinding
import com.reephub.praeter.ui.signup.NextViewPagerClickListener
import com.reephub.praeter.ui.signup.SignUpViewModel

class PlanFragment : Fragment(), View.OnClickListener {

    private var _viewBinding: FragmentPlanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val mViewModel: SignUpViewModel by activityViewModels()

    private lateinit var mListener: NextViewPagerClickListener


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
        binding.btnContinue.setOnClickListener(this)
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

        mListener.onNextViewPagerClicked()
    }

    companion object {
        val TAG = PlanFragment::class.java.simpleName

        fun newInstance(): PlanFragment {
            val args = Bundle()
            val fragment = PlanFragment()
            fragment.arguments = args
            return fragment
        }
    }
}