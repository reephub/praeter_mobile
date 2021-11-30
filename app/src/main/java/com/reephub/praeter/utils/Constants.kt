package com.reephub.praeter.utils

import android.annotation.SuppressLint
import com.reephub.praeter.core.utils.PraeterDeviceManager
import java.util.*

object Constants {

    private const val HTTP = "http://"
//    private const val IP_ADDRESS = "0.0.0.0"
    private const val IP_ADDRESS = "10.188.197.205"
    private const val EMULATOR_IP_ADDRESS = "10.0.2.2"
    private const val PORT = ":8080"
    private const val SEPARATOR = "/"

    //REST client Base URL
    @SuppressLint("ConstantLocale")
    val BASE_ENDPOINT_PRAETER_URL =
        HTTP +
                if (PraeterDeviceManager.getModel().trim().lowercase(Locale.getDefault())
                        .contains("sdk")
                ) EMULATOR_IP_ADDRESS
                else IP_ADDRESS +
                        PORT
}