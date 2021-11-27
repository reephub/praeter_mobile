package com.reephub.praeter.data.local

import com.reephub.praeter.data.local.dao.UserDao
import com.reephub.praeter.data.local.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DbImpl @Inject constructor(
    userDao: UserDao
) : IDb {

    private var mUserDao: UserDao = userDao

    override fun insertUser(user: User) {
        mUserDao.insert(user)
    }

    override fun getUser(): Flow<User> {
        return mUserDao.getUser()
    }

    override fun deleteAll() {
        mUserDao.deleteAll()
    }
}