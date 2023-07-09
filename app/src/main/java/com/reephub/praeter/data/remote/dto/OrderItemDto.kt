package com.reephub.praeter.data.remote.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@Serializable
data class OrderItemDto @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("item") val item: String,
    @JsonNames("amount") val amount: Int,
    @JsonNames("price") val price: Double
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
