package com.reephub.praeter.data.remote

import com.reephub.praeter.data.remote.api.*
import com.reephub.praeter.data.remote.dto.*
import com.reephub.praeter.data.remote.dto.directions.GoogleDirectionsResponse
import javax.inject.Inject

class ApiImpl @Inject constructor(
    dbApiService: DbApiService,
    userApiService: UserApiService,
    orderApiService: OrderApiService,
    classesApiService: ClassesApiService,
    ancientApiService: AncientApiService,
    googleDirectionsApiService: GoogleDirectionsApiService
) : IApi {

    private var mDbApiService: DbApiService = dbApiService
    private var mUserApiService: UserApiService = userApiService
    private var mOrderApiService: OrderApiService = orderApiService
    private var mClassesApiService: ClassesApiService = classesApiService
    private var mAncientApiService: AncientApiService = ancientApiService
    private var mGoogleDirectionsApiService: GoogleDirectionsApiService = googleDirectionsApiService

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
    override suspend fun getDirections(
        origin: String,
        destination: String
    ): GoogleDirectionsResponse = mGoogleDirectionsApiService.getDirections(origin, destination)

    override suspend fun login(user: UserDto): LoginResponse = mUserApiService.login(user)

    override suspend fun saveUser(user: UserDto): UserResponse = mUserApiService.saveUser(user)


    /*override suspend fun login(user: User) {
        mUserApiService.login(user)
    }*/

}