package com.reephub.praeter.ui.login

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reephub.praeter.R
import com.reephub.praeter.core.compose.annotation.DevicePreviews
import com.reephub.praeter.core.compose.theme.PraeterTheme
import com.reephub.praeter.core.compose.utils.findActivity
import com.reephub.praeter.core.compose.utils.isPreview
import com.reephub.praeter.data.local.model.LoginUiState
import com.reephub.praeter.ui.mainactivity.MainActivity
import kotlinx.coroutines.delay
import timber.log.Timber

/////////////////////////////////////
//
// COMPOSE
//
/////////////////////////////////////
@Composable
fun LoginContent(viewModel: LoginViewModel) {
    val context = LocalContext.current

    val status by viewModel.loginUiState.collectAsStateWithLifecycle()

    PraeterTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_praeter_logo),
                contentDescription = "praeter logo"
            )

            AnimatedVisibility(visible = if (isPreview()) true else viewModel.isFormVisible) {
                Form(viewModel = viewModel)
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(500L)
        viewModel.updateIsFormVisible(true)
    }

    LaunchedEffect(status) {
        if (status is LoginUiState.Success) {
            Timber.i("LoginContent | status is LoginUiState.Success")
            delay(1500L)

            (context.findActivity() as LoginActivity).apply {
                Intent(this, MainActivity::class.java).run {
                    (context.findActivity() as LoginActivity).startActivity(this)
                }
            }
        }
    }
}


/////////////////////////////////////
//
// PREVIEWS
//
/////////////////////////////////////
@DevicePreviews
@Composable
private fun Preview() {
    val viewModel: LoginViewModel = hiltViewModel()

    PraeterTheme {
        LoginContent(viewModel = viewModel)
    }
}