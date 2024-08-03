package com.reephub.praeter.ui.signup

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reephub.praeter.core.compose.annotation.DevicePreviews
import com.reephub.praeter.core.compose.theme.PraeterTheme
import com.reephub.praeter.core.compose.utils.BackInvokeHandler
import com.reephub.praeter.core.compose.utils.findActivity
import com.reephub.praeter.core.utils.PraeterCompatibilityManager
import com.reephub.praeter.data.local.model.Screen
import com.reephub.praeter.ui.signup.plan.PlanContent
import com.reephub.praeter.ui.signup.premium.PremiumContent
import com.reephub.praeter.ui.signup.successfulsignup.SuccessfulSignUpContent
import com.reephub.praeter.ui.signup.terms.TermsContent
import com.reephub.praeter.ui.signup.userform.UserFormContent
import timber.log.Timber

/////////////////////////////////////
//
// COMPOSE
//
/////////////////////////////////////
@Composable
fun SignUpProgression(
    viewModel: SignUpViewModel,
    modifier: Modifier,
    navController: NavHostController
) {
    Timber.v("@Composable | SignUpProgression()")
    PraeterTheme {
        Row(
            modifier = Modifier.then(modifier),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            repeat(viewModel.routesIndexed.size) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LinearProgressIndicator(
                        progress = {
                            if (viewModel.currentIndex < it) 0.0f else 1.0f
                        },
                        modifier = Modifier.height(4.dp),
                    )

                    Text(
                        text = stringResource(id = viewModel.routesIndexed[it].first.resourceId),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W300
                    )
                }
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun SignUpContent(viewModel: SignUpViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val navController = rememberNavController()
    navController.apply {
        this.setLifecycleOwner(lifecycleOwner)
        this.enableOnBackPressed(true)
        this.addOnDestinationChangedListener { controller, destination, arguments ->
            Timber.i("navController.addOnDestinationChangedListener | onDestinationChanged() | ${destination.route}")

            /*if (controller.popBackStack()) {
                Timber.e("controller.popBackStack() | attempt to go back ?")
            }*/

            destination.route?.let { currentRoute ->
                val screen: Screen = Screen.findByRoute(currentRoute)
                viewModel.getIndexForRoute(screen.route)
                viewModel.updateCurrentRoute(screen)
            }
        }
    }

    PraeterTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                SignUpProgression(
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.4f),
                    navController = navController
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    NavHost(
                        navController = navController,
                        modifier = Modifier.fillMaxSize(),
                        startDestination = Screen.Terms.route,
                    ) {
                        composable(Screen.Terms.route) { TermsContent(viewModel) }
                        composable(Screen.UserForm.route) {
                            UserFormContent(
                                navController,
                                viewModel
                            )
                        }
                        composable(Screen.Plan.route) { PlanContent(navController, viewModel) }
                        composable(Screen.Premium.route) {
                            PremiumContent(
                                navController,
                                viewModel
                            )
                        }
                        composable(Screen.SuccessfulSignUp.route) {
                            SuccessfulSignUpContent(
                                navController,
                                viewModel
                            )
                        }
                    }
                }

                SignUpButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.35f),
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }

    if (PraeterCompatibilityManager.isTiramisu()) {
        BackInvokeHandler(handleBackHandler = true) {
            Timber.d("BackInvokeHandler | onBackClicked()")

            navController.popBackStack()

            navController.currentDestination?.route?.let { currentRoute ->
                val screen: Screen = Screen.findByRoute(currentRoute)
                viewModel.getIndexForRoute(screen.route)
                viewModel.updateCurrentRoute(screen)
            }
        }
    } else {
        BackHandler {
            Timber.d("BackHandler | onBackClicked()")
            (context.findActivity() as SignUpActivity).finish()
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
    val context: Context = LocalContext.current
    val viewModel: SignUpViewModel = hiltViewModel()
    val backDispatcher = (context.findActivity() as SignUpActivity).onBackPressedDispatcher

    PraeterTheme {
        SignUpContent(viewModel = viewModel)
    }
}
