package com.reephub.praeter.data.remote.api

import com.reephub.praeter.data.remote.dto.ClassesDto
import retrofit2.http.GET

interface ClassesApiService {
    @GET("/classes")
    suspend fun getClasses(): List<ClassesDto>
}