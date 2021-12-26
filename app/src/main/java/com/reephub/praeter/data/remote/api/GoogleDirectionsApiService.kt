package com.reephub.praeter.data.remote.api

import com.reephub.praeter.data.remote.dto.directions.GoogleDirectionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleDirectionsApiService {
    @GET("maps/api/directions/json")
    suspend fun getDirections(
        @Query("origin", encoded = true) origin: String,
        @Query("destination", encoded = true) destination: String,
    ): GoogleDirectionsResponse
}