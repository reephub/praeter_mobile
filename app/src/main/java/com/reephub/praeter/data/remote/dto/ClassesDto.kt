package com.reephub.praeter.data.remote.dto

data class ClassesDto(
    val id: String,
    var name: String,
    var type: String,
    var duration: String,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)
