package com.reephub.praeter.di

import android.content.Context
import androidx.room.Room
import com.reephub.praeter.data.local.PraeterDatabase
import com.reephub.praeter.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): PraeterDatabase {
        return Room
            .databaseBuilder(appContext, PraeterDatabase::class.java, PraeterDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(appDatabase: PraeterDatabase): UserDao {
        return appDatabase.getUserDao()
    }
}