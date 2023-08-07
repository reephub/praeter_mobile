package com.reephub.praeter.ui.splashscreen

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.reephub.praeter.R
import com.reephub.praeter.core.compose.annotation.DevicePreviews
import com.reephub.praeter.core.compose.theme.PraeterTheme
import com.reephub.praeter.core.compose.utils.findActivity
import com.reephub.praeter.ui.login.LoginActivity
import kotlinx.coroutines.delay
import timber.log.Timber

/////////////////////////////////////
//
// COMPOSE
//
/////////////////////////////////////
@Composable
fun SplashScreenContent(viewModel: SplashScreenViewModel) {
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }
    var loadingVisible by remember { mutableStateOf(false) }

    PraeterTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_praeter_logo),
                    contentDescription = "praeter logo"
                )
                AnimatedVisibility(visible = if (LocalInspectionMode.current) true else expanded) {
                    Text(text = stringResource(id = R.string.splash_catch_phrase))
                }
            }

            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(36.dp),
                visible = if (LocalInspectionMode.current) true else loadingVisible
            ) {
                CircularProgressIndicator()
            }
        }
    }

    LaunchedEffect(Unit) {
        Timber.d("LaunchedEffect | Unit")
        delay(250L)
        expanded = true
        delay(250L)
        loadingVisible = true
    }

    LaunchedEffect(loadingVisible) {
        Timber.d("LaunchedEffect | loadingVisible")
        delay(1500L)
        Intent(
            context.findActivity() as SplashScreenActivity,
            LoginActivity::class.java
        )
            .runCatching {
                (context.findActivity() as SplashScreenActivity).startActivity(this)
            }
            .onFailure { Timber.e(it) }
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
    val viewModel: SplashScreenViewModel = hiltViewModel()

    PraeterTheme {
        SplashScreenContent(viewModel = viewModel)
    }
}