package com.reephub.praeter.utils

import com.google.android.libraries.places.api.model.Place
import com.reephub.praeter.core.utils.PraeterDeviceManager

object Constants {


    /////////////////////////////////////////////
    //
    // Praeter API
    //
    /////////////////////////////////////////////
    private const val HTTP = "http://"

//    private const val IP_ADDRESS = "192.168.0.136"
        private const val IP_ADDRESS = "192.168.0.48"

//    private const val EMULATOR_IP_ADDRESS = "192.168.0.163"
        private const val EMULATOR_IP_ADDRESS = "192.168.0.48"

    private const val PORT = ":8100"
    private const val SEPARATOR = "/"

    val BASE_ENDPOINT_PRAETER_URL =
        HTTP + if (PraeterDeviceManager.getModel().trim().lowercase()
                .contains("sdk")
        ) EMULATOR_IP_ADDRESS else IP_ADDRESS + PORT




    /////////////////////////////////////////////
    //
    // Google Maps, Directions & Places
    //
    /////////////////////////////////////////////
    const val BASE_ENDPOINT_GOOGLE_DIRECTIONS_API = "https://maps.googleapis.com$SEPARATOR"

    const val CHRONOPOST_LOCATION: String =
        "48.819066,2.328843"
    const val BELTOISE_RACE_TRACK_LOCATION: String =
        "48.757288,1.987732"

    const val REQUESTING_LOCATION_UPDATES_KEY: String =
        "REQUESTING_LOCATION_UPDATES_KEY"
    const val REQUEST_CHECK_SETTINGS: Int = 34563

    // location updates interval - 10sec
    const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000

    /* Note: findCurrentPlace() and fetchPlace() support different sets of fields.
     * findCurrentPlace() does NOT support the following fields:
     * Place.Field.ADDRESS_COMPONENTS, Place.Field.OPENING_HOURS, Place.Field.PHONE_NUMBER,
     * Place.Field.UTC_OFFSET, and Place.Field.WEBSITE_URI.
     */
    val CURRENT_PLACE_FIELDS = listOf(
        Place.Field.ADDRESS,
        Place.Field.ID,
        Place.Field.LAT_LNG,
        Place.Field.NAME
    )

    val PLACES_FIELDS = listOf(
        Place.Field.ADDRESS,
        Place.Field.ADDRESS_COMPONENTS,
        Place.Field.BUSINESS_STATUS,
        Place.Field.ID,
        Place.Field.LAT_LNG,
        Place.Field.NAME,
        Place.Field.OPENING_HOURS,
        Place.Field.PHONE_NUMBER,
        Place.Field.TYPES,
        Place.Field.VIEWPORT,
        Place.Field.UTC_OFFSET
    )

}