package com.reephub.praeter.data.local.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import  com.reephub.praeter.R
import timber.log.Timber

@Stable
sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    @Stable
    data object Terms : Screen("terms", R.string.title_activity_license_agreement)

    @Stable
    data object UserForm : Screen("userform", R.string.title_activity_user_inscription_form)

    @Stable
    data object Plan : Screen("plan", R.string.title_activity_plan)

    @Stable
    data object Premium : Screen("premium", R.string.title_activity_premium_plan)

    @Stable
    data object SuccessfulSignUp :
        Screen("successful_signup", R.string.title_activity_successful_sign_up)

    @Stable
    data object UNKOWN : Screen("", -1)

    companion object {
        val routesIndexed: List<Pair<Screen, Int>> = listOf(
            Pair(Terms, 0),
            Pair(UserForm, 1),
            Pair(Plan, 2),
            Pair(Premium, 3),
            Pair(SuccessfulSignUp, 4)
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