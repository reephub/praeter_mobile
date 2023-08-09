package com.reephub.praeter.data.local.model

import androidx.annotation.StringRes
import  com.reephub.praeter.R
import timber.log.Timber

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Terms : Screen("terms", R.string.title_activity_license_agreement)
    object UserForm : Screen("userform", R.string.title_activity_user_inscription_form)
    object Plan : Screen("plan", R.string.title_activity_plan)
    object Premium : Screen("premium", R.string.title_activity_premium_plan)
    object SuccessfulSignUp :
        Screen("successful_signup", R.string.title_activity_successful_sign_up)

    object UNKOWN : Screen("", -1)

    companion object {
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