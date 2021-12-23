package com.reephub.praeter.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.reephub.praeter.R
import com.reephub.praeter.databinding.ActivitySplashscreenBinding
import com.reephub.praeter.ui.login.LoginActivity
import com.reephub.praeter.ui.mainactivity.MainActivity
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity(),
    CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var _viewBinding: ActivitySplashscreenBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val mViewModel: SplashScreenViewModel by viewModels()

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewBinding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModelsObservers()

        mViewModel.retrieveAppVersion(this)

        lifecycleScope.launch(coroutineContext) {
            delay(TimeUnit.SECONDS.toMillis(3))

            startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    /////////////////////////////////////
    //
    // CLASSES METHODS
    //
    /////////////////////////////////////
    @SuppressLint("SetTextI18n")
    private fun initViewModelsObservers() {
        mViewModel
            .getAppVersion()
            .observe(this@SplashScreenActivity, { appVersion ->
                Timber.d("Version : %s", appVersion)

                binding.tvAppVersion.text =
                    this@SplashScreenActivity.getString(R.string.version_placeholder, appVersion)
            })
    }

}