package com.reephub.praeter.ui.signup.successfulsignup


import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.reephub.praeter.core.compose.annotation.DevicePreviews
import com.reephub.praeter.core.compose.theme.PraeterTheme
import com.reephub.praeter.ui.signup.SignUpViewModel
import com.reephub.praeter.R


/////////////////////////////////////
//
// COMPOSE
//
/////////////////////////////////////
@Composable
fun SuccessfulSignUpContent(navController: NavHostController, viewModel: SignUpViewModel) {
    val context = LocalContext.current

    PraeterTheme {

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
    val navController = rememberNavController()
    val viewModel: SignUpViewModel = hiltViewModel()

    PraeterTheme {
        SuccessfulSignUpContent(navController = navController, viewModel = viewModel)
    }
}