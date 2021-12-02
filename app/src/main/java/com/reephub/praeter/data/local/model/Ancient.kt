package com.reephub.praeter.data.local.model

import com.reephub.praeter.data.remote.dto.AncientDto

data class Ancient(
    val id: String,
    var name: String? = null,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)


fun AncientDto.toModel(): Ancient = Ancient(id, name, latitude, longitude)
