package com.reephub.praeter.data.remote.dto.directions

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeocodedWaypoints constructor(
    @Json(name = "geocoder_status")
    val status: String,
    @Json(name = "place_id")
    val placeID: String
)
