package com.reephub.praeter.data.remote

import com.reephub.praeter.data.local.model.User

interface IApi {
    // GET
    suspend fun getDbConnection()

    // POST
    suspend fun saveUser(user: User)

    // PATCH
    suspend fun login(user: User)
}