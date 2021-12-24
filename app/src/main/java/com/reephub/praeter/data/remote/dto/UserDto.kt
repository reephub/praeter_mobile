package com.reephub.praeter.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "gender")
    val gender: String,
    @Json(name = "firstName")
    val firstName: String,
    @Json(name = "lastName")
    val lastName: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "phoneNumber")
    val phoneNumber: String,
    @Json(name = "dateOfBirth")
    val dateOfBirth: String,
    @Json(name = "isPremium")
    val isPremium: Boolean,
    @Json(name = "isCustomer")
    val isCustomer: Boolean,
    @Json(name = "isProvider")
    val isProvider: Boolean
) {

    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        false,
        false,
        false
    )

    constructor(email: String, password: String) : this(
        "",
        "",
        "",
        email,
        password,
        "",
        "",
        false,
        false,
        false
    )
}
