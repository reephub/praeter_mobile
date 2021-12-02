package com.reephub.praeter.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderItemDto(
    @Json(name = "item") val item: String,
    @Json(name = "amount") val amount: Int,
    @Json(name = "price") val price: Double
) {
    companion object {

        val orderStorage = listOf(
            OrderItemDto("Ham Sandwich", 2, 5.50),
            OrderItemDto("Water", 1, 1.50),
            OrderItemDto("Beer", 3, 2.30),
            OrderItemDto("Cheesecake", 1, 3.75),
            OrderItemDto("Cheeseburger", 1, 8.50),
            OrderItemDto("Water", 2, 1.50),
            OrderItemDto("Coke", 2, 1.76),
            OrderItemDto("Ice Cream", 1, 2.35)
        )

    }
}
