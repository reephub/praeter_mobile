package com.reephub.praeter.data.remote.dto.directions

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class TextValue @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("text") val text: String,
    @JsonNames("value") val value: Int
)
