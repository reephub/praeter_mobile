package com.reephub.praeter.data.remote.dto.directions

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Legs(
    @Json(name = "distance")
    val distance: TextValue,
    @Json(name = "duration")
    val duration: TextValue,
    @Json(name = "end_address")
    val endAddress: String,
    @Json(name = "end_location")
    val endLocation: LatLongLocation,
    @Json(name = "start_address")
    val startAddress: String,
    @Json(name = "start_location")
    val startLocation: LatLongLocation,
    @Json(name = "steps")
    val steps: List<Steps>,
){
    override fun toString(): String {
        return "Legs(distance=$distance, duration=$duration, endAddress='$endAddress', endLocation=$endLocation, startAddress='$startAddress', startLocation=$startLocation, steps=$steps)"
    }
}
