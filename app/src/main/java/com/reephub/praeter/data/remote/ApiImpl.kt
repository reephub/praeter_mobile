package com.reephub.praeter.data.remote

import com.reephub.praeter.data.local.model.User
import com.reephub.praeter.data.remote.api.DbApiService
import com.reephub.praeter.data.remote.api.UserApiService
import javax.inject.Inject

class ApiImpl @Inject constructor(
    dbApiService: DbApiService,
    userApiService: UserApiService
) : IApi {

    private var mDbApiService: DbApiService = dbApiService
    private var mUserApiService: UserApiService = userApiService

    override suspend fun getDbConnection() {
        mDbApiService.getDbConnection()
    }

    override suspend fun saveUser(user: User) {
        mUserApiService.saveUser(user)
    }

    override suspend fun login(user: User) {
        mUserApiService.login(user)
    }

}