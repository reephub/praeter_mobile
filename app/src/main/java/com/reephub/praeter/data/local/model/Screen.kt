package com.reephub.praeter.data.local.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import  com.reephub.praeter.R
import timber.log.Timber

@Stable
sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    @Stable
    object Terms : Screen("terms", R.string.title_activity_license_agreement)

    @Stable
    object UserForm : Screen("userform", R.string.title_activity_user_inscription_form)

    @Stable
    object Plan : Screen("plan", R.string.title_activity_plan)

    @Stable
    object Premium : Screen("premium", R.string.title_activity_premium_plan)

    @Stable
    object SuccessfulSignUp :
        Screen("successful_signup", R.string.title_activity_successful_sign_up)

    @Stable
    object UNKOWN : Screen("", -1)

    companion object {
        val routesIndexed: List<Pair<Screen, Int>> = listOf(
            Pair(Screen.Terms, 0),
            Pair(Screen.UserForm, 1),
            Pair(Screen.Plan, 2),
            Pair(Screen.Premium, 3),
            Pair(Screen.SuccessfulSignUp, 4)
        )
        fun findByRoute(route: String): Screen = Screen::class.sealedSubclasses
            .map {
                it.objectInstance as Screen
            }
            .firstOrNull { it.route == route }
            .let {
                Timber.d("findByRoute() | $it")
                when (it) {
                    null -> UNKOWN
                    else -> it
                }
            }
    }
}