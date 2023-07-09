package com.reephub.praeter.data.remote.dto.directions

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class LatLongLocation @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("lat")
    val latitude: Double,
    @JsonNames("lng")
    val longitude: Double
) {
    override fun toString(): String {
        return "LatLongLocation(latitude=$latitude, longitude=$longitude)"
    }
}