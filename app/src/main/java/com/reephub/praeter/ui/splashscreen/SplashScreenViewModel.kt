package com.reephub.praeter.ui.splashscreen

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

@SuppressLint("CustomSplashScreen")
class SplashScreenViewModel : ViewModel() {

    private val appVersion: MutableLiveData<String> = MutableLiveData()

    /////////////////////////////////////
    //
    // OBSERVERS
    //
    /////////////////////////////////////
    fun getAppVersion(): LiveData<String> {
        return appVersion
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

            appVersion.value = version

        } catch (error: PackageManager.NameNotFoundException) {
            Timber.e(error)
        }
    }
}