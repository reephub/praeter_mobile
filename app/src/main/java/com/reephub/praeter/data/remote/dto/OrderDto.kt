package com.reephub.praeter.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderDto(
    @Json(name = "number") val number: String,
    @Json(name = "contents") val contents: List<OrderItemDto>
)
