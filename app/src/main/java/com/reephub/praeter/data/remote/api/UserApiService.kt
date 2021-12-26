package com.reephub.praeter.data.remote.api

import com.reephub.praeter.data.remote.dto.LoginResponse
import com.reephub.praeter.data.remote.dto.UserDto
import com.reephub.praeter.data.remote.dto.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {

    @POST("/login")
    suspend fun login(@Body user: UserDto): LoginResponse

    @POST("/users")
    suspend fun saveUser(@Body user: UserDto): UserResponse

    /*@PATCH("/users/login")
    suspend fun login(@Body user: User): Unit*/
}