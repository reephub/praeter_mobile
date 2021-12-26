package com.reephub.praeter.ui.signup.userform

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.reephub.praeter.BuildConfig
import com.reephub.praeter.R
import com.reephub.praeter.databinding.FragmentUserFormBinding
import com.reephub.praeter.ui.signup.NextViewPagerClickListener
import com.reephub.praeter.ui.signup.SignUpViewModel
import timber.log.Timber

class UserFormFragment : Fragment(),
    AdapterView.OnItemSelectedListener, View.OnClickListener {

    private var _viewBinding: FragmentUserFormBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val mViewModel: SignUpViewModel by activityViewModels()

    var isPasswordVisible: Boolean = false
    private var szGender: String? = null

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
        _viewBinding = FragmentUserFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()
        setListeners()

        if (BuildConfig.DEBUG) preloadData()
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
        binding.spGender.onItemSelectedListener = this
        binding.btnPasswordVisibility.setOnClickListener(this)
        binding.btnContinue.setOnClickListener(this)
    }

    private fun setupSpinner() {
        val adapter: GenderSpinnerAdapter = GenderSpinnerAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            requireActivity().resources.getStringArray(R.array.user_form_gender).toList()
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spGender.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    private fun preloadData() {
        binding.inputLastName.setText("Doe")
        binding.inputFirstName.setText("John")
        binding.inputEmail.setText("john.doe@test.fr")
        binding.inputPassword.setText("johndoe")
        binding.inputConfirmPassword.setText("johndoe")
        binding.inputPhoneNumber.setText("06123456789")
    }

    /**
     * Validating form
     */
    private fun submitForm() {
        if (!validateGender()) {
            return
        }
        if (!validateLastName()) {
            return
        }
        if (!validateFirstName()) {
            return
        }
        if (!validateEmail()) {
            return
        }
        if (!validatePassword()) {
            return
        }
        if (!validateConfirmPassword()) {
            return
        }
        if (!validatePhone()) {
            return
        }
        if (!validateDateOfBirth()) {
            return
        }

        mViewModel.setFormUser(
            szGender!!,
            binding.inputFirstName.text.toString(),
            binding.inputLastName.text.toString(),
            binding.inputEmail.text.toString(),
            binding.inputPassword.text.toString(),
            binding.inputPhoneNumber.text.toString(),
            binding.inputDateOfBirth.text.toString()
        )

        Toast.makeText(context, "Thank You!", Toast.LENGTH_SHORT).show()

        mListener.onNextViewPagerClicked()
    }

    private fun validateGender(): Boolean {
        if (szGender.isNullOrBlank()) {
            Toast.makeText(context, "Please select a gender", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validateLastName(): Boolean {
        if (binding.inputLastName.text.toString().trim { it <= ' ' }.isEmpty()) {
            binding.inputLayoutLastName.error = context!!.getString(R.string.err_msg_form_last_name)
            requestFocus(binding.inputLastName)
            return false
        } else {
            binding.inputLayoutLastName.isErrorEnabled = false
        }
        return true
    }


    private fun validateFirstName(): Boolean {
        if (binding.inputFirstName.text.toString().trim { it <= ' ' }.isEmpty()) {
            binding.inputLayoutFirstName.error =
                context!!.getString(R.string.err_msg_form_first_name)
            requestFocus(binding.inputFirstName)
            return false
        } else {
            binding.inputLayoutFirstName.isErrorEnabled = false
        }
        return true
    }

    private fun validateEmail(): Boolean {
        val email: String = binding.inputEmail.text.toString().trim { it <= ' ' }
        if (email.isEmpty() || !isValidEmail(email)) {
            binding.inputLayoutEmail.error = context!!.getString(R.string.err_msg_form_email)
            requestFocus(binding.inputEmail)
            return false
        } else {
            binding.inputLayoutEmail.isErrorEnabled = false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        if (binding.inputPassword.text.toString().trim { it <= ' ' }.isEmpty()) {
            binding.inputLayoutPassword.error = context!!.getString(R.string.err_msg_form_password)
            requestFocus(binding.inputPassword)
            return false
        } else {
            binding.inputLayoutPassword.isErrorEnabled = false
        }
        return true
    }


    private fun validateConfirmPassword(): Boolean {
        if (binding.inputConfirmPassword.text.toString().trim { it <= ' ' }.isEmpty()
            && binding.inputConfirmPassword.text.toString() != binding.inputPassword.text.toString()
        ) {
            binding.inputLayoutConfirmPassword.error =
                context!!.getString(R.string.err_msg_form_confirm_password)
            requestFocus(binding.inputConfirmPassword)
            return false
        } else {
            binding.inputLayoutConfirmPassword.isErrorEnabled = false
        }
        return true
    }


    private fun validatePhone(): Boolean {
        if (binding.inputPhoneNumber.text.toString().trim { it <= ' ' }.isEmpty()) {
            binding.inputLayoutPhoneNumber.error =
                context!!.getString(R.string.err_msg_form_phone_number)
            requestFocus(binding.inputPhoneNumber)
            return false
        } else {
            binding.inputLayoutPhoneNumber.isErrorEnabled = false
        }
        return true
    }


    private fun validateDateOfBirth(): Boolean {
        /*if (inputDateOfBirth.text.toString().trim().isEmpty()) {
            inputLayoutDateOfBirth.setError(context.getString(R.string.err_msg_form_date_of_birth));
            requestFocus(inputDateOfBirth);
            return false;
        } else {
            inputLayoutDateOfBirth.setErrorEnabled(false);
        }
*/
        return true
    }


    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            requireActivity().window
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }


    private fun changeButtonState(isEnable: Boolean) {
        Timber.d("changeButtonState() to $isEnable")
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

    /////////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////////
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_password_visibility -> {
                Timber.d("onPasswordVisibilityButtonClicked()")

                // If flag is false - password hidden (default)
                if (!isPasswordVisible) {
                    binding.inputPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    binding.btnPasswordVisibility.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.ic_visibility_off
                        )
                    )
                    // Tint color programmatically
                    // https://stackoverflow.com/questions/20121938/how-to-set-tint-for-an-image-view-programmatically-in-android
                    binding.btnPasswordVisibility.setColorFilter(
                        ContextCompat.getColor(
                            context!!,
                            R.color.purple_200
                        ), PorterDuff.Mode.SRC_IN
                    )
                } else {
                    binding.inputPassword.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    binding.btnPasswordVisibility.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.ic_visibility
                        )
                    )
                    binding.btnPasswordVisibility.setColorFilter(
                        ContextCompat.getColor(
                            context!!,
                            R.color.white
                        ), PorterDuff.Mode.SRC_IN
                    )
                }

                isPasswordVisible = !isPasswordVisible
            }
            R.id.btn_continue -> {
                Timber.d("onContinueButtonClicked()")

                Timber.d("check if field are correctly filled")
                submitForm()
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Timber.d("Position : %s", position)

        when (position) {
            1 -> {
                szGender = "Male"
                changeButtonState(true)
            }
            2 -> {
                szGender = "Female"
                changeButtonState(true)
            }
            else -> {
                Timber.e("else")
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Timber.e("Nothing selected")
    }

    companion object {
        val TAG: String = UserFormFragment::class.java.simpleName

        fun newInstance(): UserFormFragment {
            val args = Bundle()
            val fragment = UserFormFragment()
            fragment.arguments = args
            return fragment
        }
    }

}