package com.reephub.praeter.data.local

import com.reephub.praeter.data.local.model.User
import kotlinx.coroutines.flow.Flow


interface IDb {

    /////////////////////////////////////
    //
    // USER
    //
    /////////////////////////////////////
    fun insertUser(user: User)
    fun getUser(): Flow<User>
    fun deleteAll()
}