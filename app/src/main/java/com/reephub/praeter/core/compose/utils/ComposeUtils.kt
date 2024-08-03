package com.reephub.praeter.core.compose.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.os.Build
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.reephub.praeter.ui.signup.SignUpActivity
import timber.log.Timber

fun Context.findActivity(): Activity? = when (this) {
    is AppCompatActivity -> this
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
@ReadOnlyComposable
fun resourcesAsComposable(): Resources = LocalContext.current.resources

@Composable
@ReadOnlyComposable
fun isPreview(): Boolean = LocalInspectionMode.current


@Composable
fun isKeyboardVisible(): Boolean = WindowInsets.ime.getBottom(LocalDensity.current) > 0


/*
use example :
val isKeyboardOpen by keyboardAsState() // true or false

 */
@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}

@Composable
fun keyboardAsStateView(): State<Boolean> {
    val keyboardState = remember { mutableStateOf(false) }
    val view = LocalView.current
    LaunchedEffect(view) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            keyboardState.value = insets.isVisible(WindowInsetsCompat.Type.ime())
            insets
        }
    }
    return keyboardState
}

@Composable
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun BackInvokeHandler(
    handleBackHandler: Boolean,
    priority: Int = OnBackInvokedDispatcher.PRIORITY_DEFAULT,
    callback: () -> Unit = {}
) {
    Timber.d("BackInvokeHandler | onBackClicked()")
    val backInvokedCallback = remember {
        OnBackInvokedCallback {
            callback()
        }
    }

    val activity = when (LocalLifecycleOwner.current) {
        is SignUpActivity -> LocalLifecycleOwner.current as SignUpActivity
        is Fragment -> (LocalLifecycleOwner.current as Fragment).requireActivity() as SignUpActivity
        else -> {
            val context = LocalContext.current
            if (context is SignUpActivity) {
                context
            } else {
                throw IllegalStateException("LocalLifecycleOwner is not ComponentActivity or Fragment")
            }
        }
    }

    if (handleBackHandler) {
        activity.onBackInvokedDispatcher.registerOnBackInvokedCallback(
            priority,
            backInvokedCallback
        )
    }

    LaunchedEffect(handleBackHandler) {
        if (!handleBackHandler) {
            activity.onBackInvokedDispatcher.unregisterOnBackInvokedCallback(backInvokedCallback)
        }
    }

    DisposableEffect(activity.lifecycle, activity.onBackInvokedDispatcher) {
        onDispose {
            activity.onBackInvokedDispatcher.unregisterOnBackInvokedCallback(backInvokedCallback)
        }
    }
}

/*
fun NavController.popBackStack(route: String): Boolean {
    if (backQueue.isEmpty()) {
        return false
    }

    var found = false
    var popCount = 0
    val iterator = backQueue.reversed().iterator()
    while (iterator.hasNext()) {
        val entry = iterator.next()
        popCount++
        val intent = entry
            .arguments
            ?.get("android-support-nav:controller:deepLinkIntent") as Intent?
        if (intent?.data?.toString() == "android-app://androidx.navigation/$route") {
            found = true
            break
        }
    }

    if (found) {
        navigate(route) {
            while (popCount-- > 0) {
                popBackStack()
            }
        }
    }
    return found
}*/
