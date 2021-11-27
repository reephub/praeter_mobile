package com.reephub.praeter.di

import com.reephub.praeter.data.IRepository
import com.reephub.praeter.data.RepositoryImpl
import com.reephub.praeter.data.local.DbImpl
import com.reephub.praeter.data.local.PraeterDatabase
import com.reephub.praeter.data.remote.ApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class) // this is new
object AppHelperModule {

    @Provides
    fun provideDbHelper(appDatabase: PraeterDatabase) = DbImpl(appDatabase.getUserDao())

    @Provides
    fun provideApiHelper() =
        ApiImpl(
            ApiModule.provideDbAPIService(),
            ApiModule.provideUserAPIService(),
        )

    @Provides
    @ViewModelScoped // this is new
    fun provideRepository(dbImpl: DbImpl, apiImpl: ApiImpl) =
        RepositoryImpl(dbImpl, apiImpl) as IRepository
}