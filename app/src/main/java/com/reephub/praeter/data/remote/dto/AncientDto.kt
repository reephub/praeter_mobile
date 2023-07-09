package com.reephub.praeter.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class AncientDto(
    val id: String,
    var name: String? = null,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)
