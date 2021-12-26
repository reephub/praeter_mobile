package com.reephub.praeter.data.remote.dto.directions

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Steps(
    @Json(name = "distance")
    val distance: TextValue,
    @Json(name = "duration")
    val duration: TextValue,
    @Json(name = "end_location")
    val endLocation: LatLongLocation,
    @Json(name = "start_location")
    val startLocation: LatLongLocation,
    @Json(name = "html_instructions")
    val htmlInstruction: String,
    @Json(name = "polyline")
    val polyline: Polyline,
    @Json(name = "travel_mode")
    val travelMode: String,
){
    override fun toString(): String {
        return "Steps(distance=$distance, duration=$duration, endLocation=$endLocation, startLocation=$startLocation, htmlInstruction='$htmlInstruction', polyline=$polyline, travelMode='$travelMode')"
    }
}
