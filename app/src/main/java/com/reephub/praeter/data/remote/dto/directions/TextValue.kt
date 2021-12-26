package com.reephub.praeter.data.remote.dto.directions

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TextValue(
    @Json(name = "text")
    val text: String,
    @Json(name = "value")
    val value: Int
)
