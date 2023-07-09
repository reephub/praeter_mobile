package com.reephub.praeter.data.remote.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class OrderDto @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("number") val number: String,
    @JsonNames("contents") val contents: List<OrderItemDto>
)
