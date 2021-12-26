package com.reephub.praeter.data.remote

import com.reephub.praeter.data.remote.dto.*
import com.reephub.praeter.data.remote.dto.directions.GoogleDirectionsResponse

interface IApi {
    // GET
    suspend fun getDbConnection()

    suspend fun getOrders(): List<OrderDto>

    suspend fun getLocalOrders(): List<OrderItemDto>

    suspend fun getClasses(): List<ClassesDto>

    suspend fun getAncients(): List<AncientDto>

    suspend fun getDirections(
        origin: String,
        destination: String
    ): GoogleDirectionsResponse


    // POST
    suspend fun login(user: UserDto): LoginResponse
    suspend fun saveUser(user: UserDto): UserResponse

    // PATCH
//    suspend fun login(user: User)
}