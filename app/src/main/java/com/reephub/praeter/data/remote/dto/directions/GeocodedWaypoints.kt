package com.reephub.praeter.data.remote.dto.directions

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class GeocodedWaypoints @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("geocoder_status") val status: String,
    @JsonNames("place_id") val placeID: String
)
