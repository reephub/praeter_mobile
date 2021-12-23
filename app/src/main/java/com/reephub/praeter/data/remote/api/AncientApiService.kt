package com.reephub.praeter.data.remote.api

import com.reephub.praeter.data.remote.dto.AncientDto
import retrofit2.http.GET

interface AncientApiService {
    @GET("/ancient")
    suspend fun getAncients(): List<AncientDto>
}