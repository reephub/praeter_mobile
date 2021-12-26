package com.reephub.praeter.data.remote.dto.directions

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class GoogleDirectionsResponse constructor(
    @Json(name = "geocoded_waypoints")
    val geocodedWaypoints: List<GeocodedWaypoints>,

    @Json(name = "routes")
    val routes: List<Routes>,

    @Json(name = "status")
    val status: String
) {
    override fun toString(): String {
        return "GoogleDirectionsResponse(geocodedWaypoints=$geocodedWaypoints, routes=$routes, status='$status')"
    }
}
