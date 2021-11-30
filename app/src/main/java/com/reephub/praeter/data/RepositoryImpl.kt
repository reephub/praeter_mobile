package com.reephub.praeter.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.reephub.praeter.data.local.DbImpl
import com.reephub.praeter.data.local.model.User
import com.reephub.praeter.data.remote.ApiImpl
import com.reephub.praeter.data.remote.dto.OrderDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    dbImpl: DbImpl,
    apiImpl: ApiImpl
) : IRepository {

    private var mDbImpl: DbImpl = dbImpl
    private var mApiImpl: ApiImpl = apiImpl

    /////////////////////////////////
    //
    // LIVEDATA
    //
    /////////////////////////////////
    private val mLocationData: MediatorLiveData<Boolean>
        get() = MediatorLiveData()

    override fun getLocationStatusData(): LiveData<Boolean> {
        return mLocationData
    }

    override fun addLocationStatusDataSource(data: LiveData<Boolean>) {
        mLocationData.addSource(data, mLocationData::setValue)
    }

    override fun removeLocationStatusDataSource(data: LiveData<Boolean>) {
        mLocationData.removeSource(data)
    }


    /////////////////////////////////
    //
    // DB
    //
    /////////////////////////////////
    override fun insertUser(user: User) {
        mDbImpl.insertUser(user)
    }

    override fun getUser(): Flow<User> {
        return mDbImpl.getUser()
    }

    override fun deleteAll() {
        mDbImpl.deleteAll()
    }


    /////////////////////////////////
    //
    // API
    //
    /////////////////////////////////
    override suspend fun getDbConnection() {
        mApiImpl.getDbConnection()
    }

    override suspend fun getOrders(): List<OrderDto> {
        return mApiImpl.getOrders()
    }

    override suspend fun saveUser(user: User) {
        mApiImpl.saveUser(user)
    }

    override suspend fun login(user: User) {
        mApiImpl.login(user)
    }
}