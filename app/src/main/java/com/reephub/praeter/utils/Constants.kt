package com.reephub.praeter.utils

import com.reephub.praeter.core.utils.PraeterDeviceManager

object Constants {

    private const val HTTP = "http://"

    private const val IP_ADDRESS = "192.168.0.136"
    //    private const val IP_ADDRESS = "192.168.0.48"

    private const val EMULATOR_IP_ADDRESS = "192.168.0.163"
    //    private const val EMULATOR_IP_ADDRESS = "192.168.0.48"

    private const val PORT = ":8100"
    private const val SEPARATOR = "/"

    //REST client Base URL
    val BASE_ENDPOINT_PRAETER_URL =
        HTTP + if (PraeterDeviceManager.getModel().trim().lowercase()
                .contains("sdk")
        ) EMULATOR_IP_ADDRESS else IP_ADDRESS + PORT
}