package com.reephub.praeter.data

import androidx.lifecycle.LiveData
import com.reephub.praeter.data.local.IDb
import com.reephub.praeter.data.remote.IApi

interface IRepository : IDb, IApi {

    fun getLocationStatusData(): LiveData<Boolean>

    fun addLocationStatusDataSource(data: LiveData<Boolean>)

    fun removeLocationStatusDataSource(data: LiveData<Boolean>)
}