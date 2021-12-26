package com.reephub.praeter.data.remote.dto.directions

import com.reephub.praeter.data.remote.dto.directions.Bounds
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Routes constructor(
    @Json(name = "bounds")
    val bounds: Bounds,

    @Json(name = "copyrights")
    val copyrights: String,

    @Json(name = "overview_polyline")
    val overviewPolyline: Polyline,

    @Json(name = "legs")
    val legs: List<Legs>,

    @Json(name = "summary")
    val summary: String
) {
    override fun toString(): String {
        return "Routes(bounds=$bounds, copyrights='$copyrights', legs=$legs, summary='$summary')"
    }
}
