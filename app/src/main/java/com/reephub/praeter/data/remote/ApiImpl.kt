package com.reephub.praeter.data.remote

import com.reephub.praeter.data.local.model.User
import com.reephub.praeter.data.remote.api.*
import com.reephub.praeter.data.remote.dto.AncientDto
import com.reephub.praeter.data.remote.dto.ClassesDto
import com.reephub.praeter.data.remote.dto.OrderDto
import com.reephub.praeter.data.remote.dto.OrderItemDto
import javax.inject.Inject

class ApiImpl @Inject constructor(
    dbApiService: DbApiService,
    userApiService: UserApiService,
    orderApiService: OrderApiService,
    classesApiService: ClassesApiService,
    ancientApiService: AncientApiService,
) : IApi {

    private var mDbApiService: DbApiService = dbApiService
    private var mUserApiService: UserApiService = userApiService
    private var mOrderApiService: OrderApiService = orderApiService
    private var mClassesApiService: ClassesApiService = classesApiService
    private var mAncientApiService: AncientApiService = ancientApiService

    override suspend fun getDbConnection() {
        mDbApiService.getDbConnection()
    }

    override suspend fun getOrders(): List<OrderDto> {
        return mOrderApiService.getOrders()
    }

    override suspend fun getLocalOrders(): List<OrderItemDto> {
        return OrderItemDto.orderStorage
    }

    override suspend fun getClasses(): List<ClassesDto> = mClassesApiService.getClasses()

    override suspend fun getAncients(): List<AncientDto> = mAncientApiService.getAncients()

    override suspend fun saveUser(user: User) {
        mUserApiService.saveUser(user)
    }

    override suspend fun login(user: User) {
        mUserApiService.login(user)
    }

}