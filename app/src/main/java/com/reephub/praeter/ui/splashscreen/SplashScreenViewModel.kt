package com.reephub.praeter.ui.splashscreen

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import timber.log.Timber

@SuppressLint("CustomSplashScreen")
class SplashScreenViewModel : ViewModel() {

    /////////////////////////////////////
    //
    // Composable states
    //
    /////////////////////////////////////
    private var appVersion: String? by mutableStateOf(null)
        private set

    fun updateAppVersion(version: String) {
        this.appVersion = version
    }

    /////////////////////////////////////
    //
    // CLASSES METHODS
    //
    /////////////////////////////////////
    fun retrieveAppVersion(activity: SplashScreenActivity) {
        try {
            val pInfo: PackageInfo =
                activity
                    .packageManager
                    .getPackageInfo(activity.packageName, 0)
            val version = pInfo.versionName

            updateAppVersion(version)
        } catch (error: PackageManager.NameNotFoundException) {
            Timber.e(error)
        }
    }
}