package com.reephub.praeter.data.remote.dto.directions

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
class Polyline(
    @JsonNames("points") val points: String
)