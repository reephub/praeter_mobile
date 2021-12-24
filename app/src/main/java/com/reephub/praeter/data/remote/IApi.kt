package com.reephub.praeter.data.remote

import com.reephub.praeter.data.local.model.User
import com.reephub.praeter.data.remote.dto.*
import okhttp3.Response
import okhttp3.ResponseBody

interface IApi {
    // GET
    suspend fun getDbConnection()

    suspend fun getOrders(): List<OrderDto>

    suspend fun getLocalOrders(): List<OrderItemDto>

    suspend fun getClasses(): List<ClassesDto>

    suspend fun getAncients(): List<AncientDto>

    // POST
    suspend fun login(user: UserDto): LoginResponse
    suspend fun saveUser(user: User)

    // PATCH
//    suspend fun login(user: User)
}