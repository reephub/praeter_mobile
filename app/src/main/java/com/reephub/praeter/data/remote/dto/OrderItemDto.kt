package com.reephub.praeter.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderItemDto(
    @Json(name = "item") val item: String,
    @Json(name = "amount") val amount: Int,
    @Json(name = "price") val price: Double
)
