package com.reephub.praeter.data.remote.dto

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class AncientDto(
    val id: String,
    var name: String? = null,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) : Parcelable
