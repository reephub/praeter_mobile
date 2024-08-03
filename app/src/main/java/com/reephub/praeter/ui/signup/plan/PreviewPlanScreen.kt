package com.reephub.praeter.ui.signup.plan


import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
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
fun PlanContent(navController: NavHostController, viewModel: SignUpViewModel) {
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
        PlanContent(navController, viewModel)
    }
}