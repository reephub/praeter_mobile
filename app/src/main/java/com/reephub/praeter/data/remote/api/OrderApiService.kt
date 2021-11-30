package com.reephub.praeter.data.remote.api

import com.reephub.praeter.data.remote.dto.OrderDto
import retrofit2.http.GET

interface OrderApiService {
    @GET("/order")
    suspend fun getOrders(): List<OrderDto>
}