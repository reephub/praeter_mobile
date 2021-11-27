package com.reephub.praeter.data.remote.api

import retrofit2.http.GET

interface DbApiService {
    @GET("/db")
    suspend fun getDbConnection(): Unit
}