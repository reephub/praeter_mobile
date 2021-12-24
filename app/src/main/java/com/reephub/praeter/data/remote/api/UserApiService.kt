package com.reephub.praeter.data.remote.api

import com.reephub.praeter.data.local.model.User
import com.reephub.praeter.data.remote.dto.LoginResponse
import com.reephub.praeter.data.remote.dto.UserDto
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {

    @POST("/login")
    suspend fun login(@Body user: UserDto): LoginResponse

    @POST("/users")
    suspend fun saveUser(@Body user: User): Unit

    /*@PATCH("/users/login")
    suspend fun login(@Body user: User): Unit*/
}