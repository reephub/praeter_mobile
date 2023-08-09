package com.reephub.praeter.ui.signup.premium

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.reephub.praeter.R
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.reephub.praeter.core.compose.annotation.DevicePreviews
import com.reephub.praeter.core.compose.theme.PraeterTheme
import com.reephub.praeter.ui.signup.SignUpViewModel

/////////////////////////////////////
//
// COMPOSE
//
/////////////////////////////////////
@Composable
fun PremiumContent(navController: NavHostController, viewModel: SignUpViewModel) {

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
        PremiumContent(navController = navController, viewModel = viewModel)
    }
}