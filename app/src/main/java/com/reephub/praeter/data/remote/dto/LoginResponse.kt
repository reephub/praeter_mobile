package com.reephub.praeter.data.remote.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class LoginResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("message") val message: String
)
