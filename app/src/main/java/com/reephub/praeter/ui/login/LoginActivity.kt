package com.reephub.praeter.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.reephub.praeter.BuildConfig
import com.reephub.praeter.R
import com.reephub.praeter.core.utils.UIManager
import com.reephub.praeter.data.remote.dto.UserDto
import com.reephub.praeter.databinding.ActivityLoginBinding
import com.reephub.praeter.ui.mainactivity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(),
    CoroutineScope,
    View.OnClickListener, TextView.OnEditorActionListener {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var _viewBinding: ActivityLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    var isPasswordVisible: Boolean = false

    private val mViewModel: LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
        initViewModelObservers()

        if (BuildConfig.DEBUG) {
            preloadData()
        }

        lifecycleScope.launch(coroutineContext) {
            delay(TimeUnit.SECONDS.toMillis(1))
            binding.motionLayout.transitionToEnd()
        }
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    private fun setListeners() {
        Timber.d("setListeners()")
        binding.inputEmail.addTextChangedListener(MyTextWatcher(binding.inputEmail))
        binding.inputPassword.addTextChangedListener(MyTextWatcher(binding.inputPassword))
        binding.inputPassword.setOnEditorActionListener(this)

        binding.btnPasswordVisibility.setOnClickListener(this)
        binding.btnNoAccountRegister.setOnClickListener(this)
        binding.btnEnter.setOnClickListener(this)
    }

    private fun initViewModelObservers() {
        Timber.d("initViewModelObservers()")
        mViewModel.getLogin().observe(this, {
            when (it.message) {
                "Login okay" -> {
                    onLoginSuccessful()
                }
                "Not Found" -> {
                    onLoginFailed()
                }
                else -> {
                    Timber.e("else, ${it.message}")
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    fun preloadData() {
        Timber.d("preloadData()")
        binding.inputEmail.setText("janedoe@test.fr")
        binding.inputPassword.setText("test")
    }

    private fun onLoginSuccessful() {
        Timber.d("onLoginSuccessful()")
        callMainActivity()
        finish()
    }

    private fun onLoginFailed() {
        Timber.e("onLoginFailed()")
        binding.inputLayoutEmail.error = getString(R.string.err_msg_wrong_email_or_password)
        binding.inputLayoutPassword.error = getString(R.string.err_msg_wrong_email_or_password)
    }

    /**
     * logging in user. Will make http post request with name, email
     * as parameters
     */
    private fun login() {
        Timber.e("login()")

        if (!validateEmail()) {
            return
        }
        if (!validatePassword()) {
            return
        }

        UIManager.hideKeyboard(this, findViewById(android.R.id.content))

        val email: String = binding.inputEmail.text.toString()
        val password: String = binding.inputPassword.text.toString()

        Timber.d("make rest call login")

        mViewModel.makeCallLogin(UserDto(email, password))
    }


    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    // Validating email
    private fun validateEmail(): Boolean {
        val email: String = binding.inputEmail.text.toString().trim { it <= ' ' }
        if (email.isEmpty() || !isValidEmail(email)) {
            binding.inputLayoutEmail.error = getString(R.string.err_msg_email)
            requestFocus(binding.inputEmail)
            return false
        } else {
            binding.inputLayoutEmail.isErrorEnabled = false
        }
        return true
    }


    // Validating password
    private fun validatePassword(): Boolean {
        if (binding.inputPassword.text.toString().trim { it <= ' ' }.isEmpty()) {
            binding.inputLayoutPassword.error = getString(R.string.err_msg_name)
            requestFocus(binding.inputPassword)
            return false
        } else {
            binding.inputLayoutPassword.isErrorEnabled = false
        }
        return true
    }


    private fun isValidEmail(email: String): Boolean {
        return (!TextUtils.isEmpty(email)
                && Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }


    fun callMainActivity() {
        Timber.d("callMainActivity()")
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
    }

    fun callSignUpActivity() {
        Timber.d("callSignUpActivity()")

    }

    override fun onClick(view: View?) {

        when (view?.id) {
            R.id.btn_password_visibility -> {
                Timber.d("onPasswordVisibilityButtonClicked()")

                // If flag is false - password hidden (default)

                // If flag is false - password hidden (default)
                if (!isPasswordVisible) {
                    binding.inputPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    binding.btnPasswordVisibility.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_visibility_off
                        )
                    )
                    // Tint color programmatically
                    // https://stackoverflow.com/questions/20121938/how-to-set-tint-for-an-image-view-programmatically-in-android
                    binding.btnPasswordVisibility.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.purple_200
                        ), PorterDuff.Mode.SRC_IN
                    )
                } else {
                    binding.inputPassword.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    binding.btnPasswordVisibility.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_visibility
                        )
                    )
                    binding.btnPasswordVisibility.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.white
                        ), PorterDuff.Mode.SRC_IN
                    )
                }

                isPasswordVisible = !isPasswordVisible
            }
            R.id.btn_enter -> {
                login()
            }
            R.id.btn_no_account_register -> {
                callSignUpActivity()
            }
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            UIManager.hideKeyboard(this, findViewById(android.R.id.content))
            return true
        }
        return false
    }


    inner class MyTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Ignored
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Ignored
        }

        override fun afterTextChanged(s: Editable?) {
            when (view.id) {
                R.id.input_password -> validatePassword()
                R.id.input_email -> validateEmail()
            }
        }

    }
}