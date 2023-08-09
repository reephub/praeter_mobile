package com.reephub.praeter.ui.signup

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class SignUpActivity : ComponentActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private val mViewModel: SignUpViewModel by viewModels()

    // private lateinit var onBackInvokedDispatcher : OnBackPressedDispatcher

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModelsObservers()

        /*if (PraeterCompatibilityManager.isTiramisu()) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {

                BackPressed()
            }
        } else {*/
        /*onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backPressed()
                }
            })*/
//        }

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

    private fun backPressed() {
        Timber.e("backPressed()")
    }
}