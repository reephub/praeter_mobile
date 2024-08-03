package com.reephub.praeter.ui.signup

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.reephub.praeter.ui.base.BaseComponentActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class SignUpActivity : BaseComponentActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private val mViewModel: SignUpViewModel by viewModels()


    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModelsObservers()

        lifecycleScope.launch(coroutineContext) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        SignUpContent(mViewModel)
                    }
                }
            }
        }
    }


    /////////////////////////////////////
    //
    // CLASSES METHODS
    //
    /////////////////////////////////////
    @SuppressLint("SetTextI18n")
    private fun initViewModelsObservers() {
        Timber.d("initViewModelsObservers()")
    }

    override fun backPressed() {
        Timber.e("backPressed()")
        finish()
    }
}