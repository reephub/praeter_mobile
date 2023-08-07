package com.reephub.praeter.data.remote.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class UserDto @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("gender")
    var gender: String,
    @JsonNames("firstName")
    var firstName: String,
    @JsonNames("lastName")
    var lastName: String,
    @JsonNames("email")
    var email: String,
    @JsonNames("password")
    var password: String,
    @JsonNames("phoneNumber")
    var phoneNumber: String,
    @JsonNames("dateOfBirth")
    var dateOfBirth: String,
    @JsonNames("isPremium")
    var isPremium: Boolean = false,
    @JsonNames("isCustomer")
    var isCustomer: Boolean = false,
    @JsonNames("isProvider")
    var isProvider: Boolean = false,
    @JsonNames("token")
    var token: String
): java.io.Serializable {

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
        false,
        ""
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
        false,
        ""
    )

    constructor(
        gender: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phoneNumber: String,
        dateOfBirth: String
    ) : this(
        gender,
        firstName,
        lastName,
        email,
        password,
        phoneNumber,
        dateOfBirth,
        false,
        false,
        false,
        ""
    )
}
