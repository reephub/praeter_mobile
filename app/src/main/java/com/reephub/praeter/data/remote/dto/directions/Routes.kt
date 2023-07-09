package com.reephub.praeter.data.remote.dto.directions

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
class Routes constructor(
    @JsonNames("bounds")
    val bounds: Bounds,

    @JsonNames("copyrights")
    val copyrights: String,

    @JsonNames("overview_polyline")
    val overviewPolyline: Polyline,

    @JsonNames("legs")
    val legs: List<Legs>,

    @JsonNames("summary")
    val summary: String
) {
    override fun toString(): String {
        return "Routes(bounds=$bounds, copyrights='$copyrights', legs=$legs, summary='$summary')"
    }
}
