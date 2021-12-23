package com.reephub.praeter.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ClassesDto(
    val id: String,
    var name: String,
    var type: String,
    var duration: String,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)
