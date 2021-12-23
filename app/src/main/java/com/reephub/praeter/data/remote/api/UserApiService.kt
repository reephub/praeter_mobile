package com.reephub.praeter.data.remote.api

import com.reephub.praeter.data.local.model.User
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserApiService {

    @POST("/login")
    suspend fun login(@Body user: User): Response

    @POST("/users")
    suspend fun saveUser(@Body user: User): Unit

    /*@PATCH("/users/login")
    suspend fun login(@Body user: User): Unit*/
}