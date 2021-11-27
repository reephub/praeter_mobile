package com.reephub.praeter.core.utils

import android.os.Build
import timber.log.Timber

class PraeterDeviceManager private constructor() {

    companion object {
        fun logDeviceInfo() {
            Timber.d("logDeviceInfo()")
            Timber.i("SERIAL: %s ", Build.SERIAL)
            Timber.i("MODEL: %s ", Build.MODEL)
            Timber.i("ID: %s ", Build.ID)
            Timber.i("Manufacture: %s ", Build.MANUFACTURER)
            Timber.i("brand: %s ", Build.BRAND)
            Timber.i("type: %s ", Build.TYPE)
            Timber.i("user: %s ", Build.USER)
            Timber.i("BASE: %s ", Build.VERSION_CODES.BASE)
            Timber.i("INCREMENTAL: %s ", Build.VERSION.INCREMENTAL)
            Timber.i("SDK : %s ", Build.VERSION.SDK)
            Timber.i("BOARD: %s ", Build.BOARD)
            Timber.i("BRAND: %s ", Build.BRAND)
            Timber.i("HOST: %s ", Build.HOST)
            Timber.i("FINGERPRINT: %s ", Build.FINGERPRINT)
            Timber.i("Version Code: %s ", Build.VERSION.RELEASE)
        }

        fun getSerial(): String? {
            return Build.SERIAL
        }

        fun getModel(): String {
            return Build.MODEL
        }

        fun getID(): String {
            return Build.ID
        }

        fun getManufacturer(): String {
            return Build.MANUFACTURER
        }

        fun getBrand(): String {
            return Build.BRAND
        }

        fun getType(): String {
            return Build.TYPE
        }

        fun getUser(): String {
            return Build.USER
        }

        fun getVersionBase(): Int {
            return Build.VERSION_CODES.BASE
        }

        fun getVersionIncremental(): String {
            return Build.VERSION.INCREMENTAL
        }

        fun getSdkVersion(): String {
            return Build.VERSION.SDK
        }

        fun getBoard(): String {
            return Build.BOARD
        }

        fun getHost(): String {
            return Build.HOST
        }

        fun getFingerPrint(): String {
            return Build.FINGERPRINT
        }

        fun getVersionCode(): String {
            return Build.VERSION.RELEASE
        }

    }

}