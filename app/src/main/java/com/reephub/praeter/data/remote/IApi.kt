package com.reephub.praeter.data.remote

import com.reephub.praeter.data.local.model.User
import com.reephub.praeter.data.remote.dto.OrderDto

interface IApi {
    // GET
    suspend fun getDbConnection()

    suspend fun getOrders(): List<OrderDto>

    // POST
    suspend fun saveUser(user: User)

    // PATCH
    suspend fun login(user: User)
}