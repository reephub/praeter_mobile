package com.reephub.praeter.di

import com.reephub.praeter.data.local.bean.TimeOut
import com.reephub.praeter.data.remote.api.DbApiService
import com.reephub.praeter.data.remote.api.UserApiService
import com.reephub.praeter.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.annotations.NotNull
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ApiModule {

    @Provides
    fun provideOkHttpLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message: String -> Timber.tag("OkHttp").d(message) }
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    /* Provide OkHttp for the app */
    @Provides
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(TimeOut.TIME_OUT_READ.value.toLong(), TimeUnit.SECONDS)
            .connectTimeout(TimeOut.TIME_OUT_CONNECTION.value.toLong(), TimeUnit.SECONDS)
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val original = chain.request()
                // Customize the request
                val request = original.newBuilder()
                    .header(
                        Headers.headersOf("content-type").toString(),
                        "application/json; charset=utf-8"
                    )
                    .header(
                        Headers.headersOf("connection").toString(),
                        "close"
                    )
                    .header(
                        Headers.headersOf("accept-encoding").toString(),
                        "Identity"
                    )
                    .build()
                val response = chain.proceed(request)
                response.cacheResponse
                response
            })
            .addInterceptor(provideOkHttpLogger())
            .build()
    }


    /* Provide Retrofit for the app */
    @Provides
    fun provideRetrofit(url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(provideOkHttp())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @NotNull
    fun provideDbAPIService(): DbApiService {
        return provideRetrofit(Constants.BASE_ENDPOINT_PRAETER_URL)
            .create(DbApiService::class.java)
    }

    @Provides
    @Singleton
    @NotNull
    fun provideUserAPIService(): UserApiService {
        return provideRetrofit(Constants.BASE_ENDPOINT_PRAETER_URL)
            .create(UserApiService::class.java)
    }

}