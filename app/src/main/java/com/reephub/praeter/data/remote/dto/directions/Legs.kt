package com.reephub.praeter.data.remote.dto.directions

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class Legs @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("distance")
    val distance: TextValue,
    @JsonNames("duration")
    val duration: TextValue,
    @JsonNames("end_address")
    val endAddress: String,
    @JsonNames("end_location")
    val endLocation: LatLongLocation,
    @JsonNames("start_address")
    val startAddress: String,
    @JsonNames("start_location")
    val startLocation: LatLongLocation,
    @JsonNames("steps")
    val steps: List<Steps>,
) {
    override fun toString(): String {
        return "Legs(distance=$distance, duration=$duration, endAddress='$endAddress', endLocation=$endLocation, startAddress='$startAddress', startLocation=$startLocation, steps=$steps)"
    }
}
