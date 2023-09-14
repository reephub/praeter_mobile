package com.reephub.praeter.ui.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.reephub.praeter.R
import com.reephub.praeter.core.compose.annotation.DevicePreviews
import com.reephub.praeter.core.compose.theme.PraeterTheme
import com.reephub.praeter.core.utils.UIManager
import com.reephub.praeter.data.local.model.Screen
import timber.log.Timber

@Composable
fun SignUpButton(modifier: Modifier, navController: NavController, viewModel: SignUpViewModel) {

    val context = LocalContext.current

    PraeterTheme {
        Column(
            modifier = Modifier.then(modifier),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    Timber.d("current route: ${viewModel.currentRoute.route}")

                    runCatching {
                        Timber.d("runCatching | attempt to navigate to next destination")

                        val index = viewModel.getIndexForRoute(viewModel.currentRoute.route)
                        val canGoNext = viewModel.currentRoute != Screen.SuccessfulSignUp

                        if (!canGoNext) {
                            Timber.e("runCatching | Cannot go to next screen")
                            Timber.d("runCatching | Sign up successful")
                        } else {
                            if (viewModel.currentRoute == Screen.Terms && !viewModel.isTermsChecked) {
                                Timber.d("runCatching | Checkbox not checked display toast message")
                                UIManager.showActionInToast(
                                    context,
                                    context.getString(R.string.err_msg_license_agreement_approval_mandatory)
                                )
                            } else if (viewModel.currentRoute == Screen.UserForm && viewModel.formCompleted) {
                                if(viewModel.submitForm()) {
                                    navigateToNextRoute(navController, viewModel, index)
                                }
                            }
                            else if (viewModel.currentRoute == Screen.Plan && !viewModel.isPremiumPlanSelected) {
                                navController.navigate(Screen.SuccessfulSignUp.route)
                            } else {
                                Timber.e("runCatching | Else branch")
                                /*val nextRoute = viewModel.routesIndexed[index + 1].first.route
                                Timber.d("  navController.navigate($nextRoute)")
                                navController.navigate(nextRoute)*/
                                navigateToNextRoute(navController, viewModel, index)
                            }
                        }
                    }
                        .onFailure {
                            it.printStackTrace()
                            Timber.e("runCatching | onFailure | error caught with message: ${it.message}")
                        }
                        .onSuccess {
                            Timber.d("runCatching | onSuccess | navigation successfully done")

                        }
                },
                enabled = when (viewModel.currentRoute) {
                    Screen.Terms -> {
                        viewModel.isTermsChecked
                    }

                    Screen.UserForm -> {
                        viewModel.formCompleted
                    }

                    else -> {
                        true
                    }
                }
            ) {
                Text(
                    modifier = Modifier,
                    text = if (viewModel.currentRoute == Screen.SuccessfulSignUp) stringResource(
                        id = R.string.btn_validate
                    ) else stringResource(id = R.string.btn_continue)
                )
            }
        }
    }
}

fun navigateToNextRoute(navController: NavController, viewModel: SignUpViewModel, index: Int){
    val nextRoute = viewModel.routesIndexed[index + 1].first.route
    Timber.d("  navController.navigate($nextRoute)")
    navController.navigate(nextRoute)
}

@DevicePreviews
@Composable
private fun PreviewSignUpButton() {
    val viewModel: SignUpViewModel = hiltViewModel()
    val navController = rememberNavController()

    PraeterTheme {
        SignUpButton(modifier = Modifier, navController = navController, viewModel = viewModel)
    }
}