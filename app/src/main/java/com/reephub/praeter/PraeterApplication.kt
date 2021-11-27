package com.reephub.praeter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDexApplication
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PraeterApplication : MultiDexApplication() {

    // Firebase
    private var mFirebaseCrashlytics: FirebaseCrashlytics? = null

    override fun onCreate() {
        super.onCreate()

        mInstance = this
        init()
    }

    private fun init() {
        LAB_PACKAGE_NAME = packageName

        if (BuildConfig.DEBUG) {
            // Timber : logging
            Timber.plant(Timber.DebugTree())
        }

        // Firebase Crashlytics
        /*mFirebaseCrashlytics = FirebaseCrashlytics.getInstance()
        mFirebaseCrashlytics?.setCrashlyticsCollectionEnabled(true)
        mFirebaseCrashlytics?.setUserId("wayne")*/

        // Mobile ADS

        // Mobile ADS
        /*MobileAds.initialize(this) { initializationStatus: InitializationStatus ->
            Timber.d(
                "initializationStatus : %s",
                initializationStatus.toString()
            )

        }*/
    }

    fun getContext(): Context {
        return super.getApplicationContext()
    }

    fun getLabPackageName(): String? {
        return LAB_PACKAGE_NAME
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    @SuppressLint("BinaryOperationInTimber")
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        if (level == TRIM_MEMORY_UI_HIDDEN) {
            notifyAppInBackground()
        }

        if (level == TRIM_MEMORY_RUNNING_LOW) {
            Timber.e(
                "The device is running much lower on memory. " +
                        "Your app is running and not killable," +
                        " but please release unused resources to improve system performance"
            )
        }
    }

    private fun notifyAppInBackground() {
        Timber.e("App went in background")
    }

    override fun onTerminate() {
        super.onTerminate()
        Timber.e("onTerminate()")
    }

    companion object {
        private var LAB_PACKAGE_NAME: String? = null
        private var mInstance: PraeterApplication? = null

        @Synchronized
        fun getInstance(): PraeterApplication {

            if (null == mInstance) {
                mInstance = PraeterApplication()
            }

            return mInstance as PraeterApplication
        }
    }


}