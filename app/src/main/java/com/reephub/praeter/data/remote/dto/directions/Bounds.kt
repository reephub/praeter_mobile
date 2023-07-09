package com.reephub.praeter.data.remote.dto.directions

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class Bounds @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("northeast")
    val northeast: LatLongLocation,
    @JsonNames( "southwest")
    val southwest: LatLongLocation
) {
    override fun toString(): String {
        return "Bounds(northeast=$northeast, southwest=$southwest)"
    }
}
