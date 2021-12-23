package com.reephub.praeter.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.reephub.praeter.data.remote.dto.UserDto

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Long,
    @ColumnInfo(name = "gender")
    val gender: String,
    @ColumnInfo(name = "firstName")
    val firstName: String,
    @ColumnInfo(name = "lastName")
    val lastName: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "password")
    val password: String,
    @ColumnInfo(name = "phoneNumber")
    val phoneNumber: String,
    @ColumnInfo(name = "dateOfBirth")
    val dateOfBirth: String,
    @ColumnInfo(name = "isPremium")
    val isPremium: Boolean,
    @ColumnInfo(name = "isCustomer")
    val isCustomer: Boolean,
    @ColumnInfo(name = "isProvider")
    val isProvider: Boolean
) {
    constructor(firstName: String, lastName: String) : this(
        -1L,
        "",
        firstName,
        lastName,
        "",
        "",
        "",
        "",
        false,
        false,
        false
    )
}

fun UserDto.toModel(): User = User(
    0L,
    gender,
    firstName,
    lastName,
    email,
    password,
    phoneNumber,
    dateOfBirth,
    isPremium,
    isCustomer,
    isProvider
)
