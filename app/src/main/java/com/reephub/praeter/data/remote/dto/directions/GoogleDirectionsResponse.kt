package com.reephub.praeter.data.remote.dto.directions

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
class GoogleDirectionsResponse constructor(
    @JsonNames("geocoded_waypoints")
    val geocodedWaypoints: List<GeocodedWaypoints>,

    @JsonNames("routesIndexed")
    val routes: List<Routes>,

    @JsonNames("status")
    val status: String
)
