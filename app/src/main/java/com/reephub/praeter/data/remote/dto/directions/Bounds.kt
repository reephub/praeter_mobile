package com.reephub.praeter.data.remote.dto.directions

import com.reephub.praeter.data.remote.dto.directions.LatLongLocation
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Bounds constructor(
    @Json(name = "northeast")
    val northeast: LatLongLocation,
    @Json(name = "southwest")
    val southwest: LatLongLocation
) {
    override fun toString(): String {
        return "Bounds(northeast=$northeast, southwest=$southwest)"
    }
}
