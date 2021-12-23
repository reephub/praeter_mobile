package com.reephub.praeter.data.remote

import com.reephub.praeter.data.local.model.User
import com.reephub.praeter.data.remote.dto.AncientDto
import com.reephub.praeter.data.remote.dto.ClassesDto
import com.reephub.praeter.data.remote.dto.OrderDto
import com.reephub.praeter.data.remote.dto.OrderItemDto
import okhttp3.Response

interface IApi {
    // GET
    suspend fun getDbConnection()

    suspend fun getOrders(): List<OrderDto>

    suspend fun getLocalOrders(): List<OrderItemDto>

    suspend fun getClasses(): List<ClassesDto>

    suspend fun getAncients(): List<AncientDto>

    // POST
    suspend fun login(user: User): Response
    suspend fun saveUser(user: User)

    // PATCH
//    suspend fun login(user: User)
}