package com.reephub.praeter.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.reephub.praeter.BuildConfig
import com.reephub.praeter.R
import com.reephub.praeter.core.utils.PraeterNetworkManagerNewAPI
import com.reephub.praeter.core.utils.UIManager
import com.reephub.praeter.ui.mainactivity.MainActivity
import com.reephub.praeter.ui.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val mViewModel: LoginViewModel by viewModels()

    private var mNetworkManager: PraeterNetworkManagerNewAPI? = null


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModelObservers()

        if (BuildConfig.DEBUG) {
            mViewModel.checkConnectivity(this@LoginActivity)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        LoginContent(viewModel = mViewModel)
                    }
                }
            }
        }
    }

    private fun initViewModelObservers() {
        Timber.d("initViewModelObservers()")
        mViewModel.getLogin().observe(this) {
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
        }

        mNetworkManager?.getConnectionState()?.observe(
            this
        ) {
            UIManager.showConnectionStatusInSnackBar(
                this,
                it
            )
        }
    }


    @SuppressLint("NewApi")
    private fun getConnectionInfo() {
        Timber.d("setListeners()")
        var isWifiConn = false
        var isMobileConn: Boolean = false
        val connectivityManager: ConnectivityManager =
            getSystemService(ConnectivityManager::class.java)
        val currentNetwork: Network? = connectivityManager.activeNetwork

        val caps = connectivityManager.getNetworkCapabilities(currentNetwork)
        val linkProperties = connectivityManager.getLinkProperties(currentNetwork)

        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Timber.e("The default network is now: $network")
            }

            override fun onLost(network: Network) {
                Timber.e("The application no longer has a default network. The last default network was $network")
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                Timber.e("The default network changed capabilities: $networkCapabilities")
            }

            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                Timber.e("The default network changed link properties: $linkProperties")
            }
        })

        connectivityManager.allNetworks.forEach { network ->
            connectivityManager.getNetworkInfo(network)?.apply {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    isWifiConn = isWifiConn or isConnected
                }
                if (type == ConnectivityManager.TYPE_MOBILE) {
                    isMobileConn = isMobileConn or isConnected
                }
            }
        }

        Timber.d("Wifi connected: $isWifiConn")
        Timber.d("Mobile connected: $isMobileConn")
    }

    private fun onLoginSuccessful() {
        Timber.d("onLoginSuccessful()")
        callMainActivity()
        finish()
    }

    private fun onLoginFailed() {
        val message = getString(R.string.err_msg_wrong_email_or_password)
        Timber.e("onLoginFailed() | message: $message")
    }


    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    // Validating email
    /*private fun validateEmail(): Boolean {
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
    }*/

    private fun isValidEmail(email: String): Boolean =
        !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun callMainActivity() {
        Timber.d("callMainActivity()")
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
    }

    private fun callSignUpActivity() {
        Timber.d("callSignUpActivity()")
        startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
    }
}