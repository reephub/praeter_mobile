package com.reephub.praeter.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import com.reephub.praeter.core.utils.PraeterCompatibilityManager

abstract class BaseComponentActivity : ComponentActivity() {

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PraeterCompatibilityManager.isTiramisu()) {
            onBackInvokedDispatcher
                .registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT) {
                    backPressed()
                }
        } else {
            onBackPressedDispatcher.addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        backPressed()
                    }
                })
        }
    }

    abstract fun backPressed()
}