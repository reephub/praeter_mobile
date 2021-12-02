package com.reephub.praeter.data.local.model

import com.reephub.praeter.data.remote.dto.ClassesDto

data class Classes(
    val id: String,
    var name: String,
    var type: String,
    var duration: String,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)

fun ClassesDto.toModel(): Classes = Classes(id, name, type, duration, latitude, longitude)
