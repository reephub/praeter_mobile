package com.reephub.praeter.data.remote.dto.directions

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LatLongLocation(
    @Json(name = "lat")
    val latitude: Double,
    @Json(name = "lng")
    val longitude: Double
) {
    override fun toString(): String {
        return "LatLongLocation(latitude=$latitude, longitude=$longitude)"
    }
}