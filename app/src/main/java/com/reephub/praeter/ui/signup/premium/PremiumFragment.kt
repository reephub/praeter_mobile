package com.reephub.praeter.ui.signup.premium

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.reephub.praeter.R
import com.reephub.praeter.databinding.FragmentPremiumPlanBinding
import com.reephub.praeter.ui.signup.NextViewPagerClickListener
import com.reephub.praeter.ui.signup.SignUpViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class PremiumFragment : Fragment(),
    CoroutineScope,
    View.OnClickListener {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var _viewBinding: FragmentPremiumPlanBinding? = null

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
        _viewBinding = FragmentPremiumPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        initViewModelsObservers()
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

    private fun initViewModelsObservers() {
        Timber.d("initViewModelsObservers()")

        mViewModel.getShowHideLoading().observe(
            requireActivity(),
            { isLoading -> if (!isLoading) hideLoading() else showLoading() }
        )

        mViewModel.getEnabledDisableUI().observe(
            requireActivity(),
            { isUIEnabled -> if (!isUIEnabled) disableUI() else enableUI() }
        )

        mViewModel.getSuccessCreditCard().observe(requireActivity(), { onSuccessfulCreditCard() })

        mViewModel.getFailedCreditCard().observe(requireActivity(), { onFailedCreditCard() })
    }

    private fun showLoading() {
        Timber.d("Loading")
        Timber.d("Verifying your bank details. Please wait...")

    }

    private fun hideLoading() {
        //if (null != dialog && dialog.isShowing()) dialog.dismiss()
    }

    private fun disableUI() {
        binding.inputLayoutCreditCardOwnerName.isEnabled = false
        binding.inputLayoutCreditCardNumber.isEnabled = false
        binding.inputLayoutCreditCardCcv.isEnabled = false
        binding.btnContinue.isEnabled = false
    }

    private fun enableUI() {

        // Avoid crash
        // Caused by: android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
        requireActivity().runOnUiThread {
            binding.inputLayoutCreditCardOwnerName.isEnabled = true
            binding.inputLayoutCreditCardNumber.isEnabled = true
            binding.inputLayoutCreditCardCcv.isEnabled = true
            binding.btnContinue.isEnabled = true
        }
    }


    private fun onSuccessfulCreditCard() {
        Snackbar
            .make(
                requireActivity().findViewById(android.R.id.content),
                "onSuccessfulCreditCard()",
                BaseTransientBottomBar.LENGTH_LONG
            )
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.success))
            .show()

        lifecycleScope.launch(coroutineContext) {
            delay(TimeUnit.SECONDS.toMillis(1))
            goToSuccessfulSignUpActivity()
        }
    }

    private fun onFailedCreditCard() {
        Snackbar
            .make(
                requireActivity().findViewById(android.R.id.content),
                "onFailedCreditCard()",
                BaseTransientBottomBar.LENGTH_LONG
            )
            .setBackgroundTint(ContextCompat.getColor(context!!, R.color.error))
            .show()
    }

    // Validating name
    private fun validateCardOwnerName(): Boolean {
        val cardOwnerName: String =
            binding.inputCreditCardOwnerName.text.toString().trim { it <= ' ' }
        if (cardOwnerName.isEmpty()) {
            binding.inputLayoutCreditCardOwnerName.error = context!!.getString(R.string.err_msg_email)
            requestFocus(binding.inputCreditCardOwnerName)
            return false
        } else {
            binding.inputLayoutCreditCardOwnerName.isErrorEnabled = false
        }
        return true
    }


    // Validating number
    private fun validateCardNumber(): Boolean {
        if (binding.inputCreditCardNumber.text.toString().trim { it <= ' ' }.isEmpty()
            || binding.inputCreditCardNumber.length() != 16
        ) {
            binding.inputLayoutCreditCardNumber.error = context!!.getString(R.string.err_msg_credit_card_number)
            requestFocus(binding.inputCreditCardNumber)
            return false
        } else {
            binding.inputLayoutCreditCardNumber.isErrorEnabled = false
        }
        return true
    }


    // Validating ccv
    private fun validateCardCCV(): Boolean {
        if (binding.inputCreditCardCcv.text.toString().trim { it <= ' ' }.isEmpty()
            || binding.inputCreditCardCcv.length() != 3
        ) {
            binding.inputLayoutCreditCardCcv.error = context!!.getString(R.string.err_msg_credit_card_ccv)
            requestFocus(binding.inputCreditCardCcv)
            return false
        } else {
            binding.inputLayoutCreditCardCcv.isErrorEnabled = false
        }
        return true
    }


    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            requireActivity().window
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    private fun onContinueButtonClicked() {
        if (!validateCardOwnerName()) {
            return
        }
        if (!validateCardNumber()) {
            return
        }

        // TODO : Check if spinner are filled
        /*if (!validateCardValidityMonth()) {
            return;
        }

        if (!validateCardValidityYear()) {
            return;
        }*/

        if (!validateCardCCV()) {
            return
        }

        // TODO : correct method : checkCreditCardInfo()
        mViewModel.checkCardValidity()
    }

    private fun goToSuccessfulSignUpActivity() = mListener.onLastViewPagerClicked()


    /////////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////////
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_continue -> {
                onContinueButtonClicked()
            }
        }
    }

    companion object {
        val TAG: String = PremiumFragment::class.java.simpleName

        fun newInstance(): PremiumFragment {
            val args = Bundle()
            val fragment = PremiumFragment()
            fragment.arguments = args
            return fragment
        }
    }

}