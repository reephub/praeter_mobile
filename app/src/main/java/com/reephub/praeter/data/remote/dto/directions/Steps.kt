package com.reephub.praeter.data.remote.dto.directions

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class Steps @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("distance")
    val distance: TextValue,
    @JsonNames("duration")
    val duration: TextValue,
    @JsonNames("end_location")
    val endLocation: LatLongLocation,
    @JsonNames("start_location")
    val startLocation: LatLongLocation,
    @JsonNames("html_instructions")
    val htmlInstruction: String,
    @JsonNames("polyline")
    val polyline: Polyline,
    @JsonNames("travel_mode")
    val travelMode: String,
) {
    override fun toString(): String {
        return "Steps(distance=$distance, duration=$duration, endLocation=$endLocation, startLocation=$startLocation, htmlInstruction='$htmlInstruction', polyline=$polyline, travelMode='$travelMode')"
    }
}
