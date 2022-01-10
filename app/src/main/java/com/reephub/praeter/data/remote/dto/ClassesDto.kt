package com.reephub.praeter.data.remote.dto

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class ClassesDto(
    val id: String,
    var name: String,
    var type: String,
    var duration: String,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) : Parcelable
