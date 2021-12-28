package com.reephub.praeter.di

import android.content.Context
import com.reephub.praeter.R
import com.reephub.praeter.data.local.bean.TimeOut
import com.reephub.praeter.data.remote.api.*
import com.reephub.praeter.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun provideOkHttp(@ApplicationContext appContext: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(TimeOut.TIME_OUT_READ.value.toLong(), TimeUnit.SECONDS)
            .connectTimeout(TimeOut.TIME_OUT_CONNECTION.value.toLong(), TimeUnit.SECONDS)
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val original = chain.request()
                // Customize the request
                val request = original.newBuilder()
                request.header("Content-Type", "application/json; charset=utf-8")
                request.header("Connection", "close") //.header("Content-Type", "application/json")
                request.header("Accept-Encoding", "Identity")

                if (original.url.host.contains("google")) {
                    val url = original.url.newBuilder()
                        .addQueryParameter("key", appContext.getString(R.string.google_maps_key))
                        .build()

                    request.url(url)
                }

                val response = chain.proceed(request.build())
                response.cacheResponse
                // Customize or return the response
                response
            })
            .addInterceptor(provideOkHttpLogger())
            .build()
    }


    /* Provide Retrofit for the app */
    @Provides
    fun provideRetrofit(url: String, @ApplicationContext appContext: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(provideOkHttp(appContext))
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .build()
    }

    @Provides
    @Singleton
    @NotNull
    fun provideDbAPIService(@ApplicationContext appContext: Context): DbApiService {
        return provideRetrofit(Constants.BASE_ENDPOINT_PRAETER_URL, appContext)
            .create(DbApiService::class.java)
    }

    @Provides
    @Singleton
    @NotNull
    fun provideUserAPIService(@ApplicationContext appContext: Context): UserApiService {
        return provideRetrofit(Constants.BASE_ENDPOINT_PRAETER_URL, appContext)
            .create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    @NotNull
    fun provideOrderAPIService(@ApplicationContext appContext: Context): OrderApiService {
        return provideRetrofit(Constants.BASE_ENDPOINT_PRAETER_URL, appContext)
            .create(OrderApiService::class.java)
    }

    @Provides
    @Singleton
    @NotNull
    fun provideClassesAPIService(@ApplicationContext appContext: Context): ClassesApiService {
        return provideRetrofit(Constants.BASE_ENDPOINT_PRAETER_URL, appContext)
            .create(ClassesApiService::class.java)
    }

    @Provides
    @Singleton
    @NotNull
    fun provideAncientAPIService(@ApplicationContext appContext: Context): AncientApiService {
        return provideRetrofit(Constants.BASE_ENDPOINT_PRAETER_URL, appContext)
            .create(AncientApiService::class.java)
    }

    @Provides
    @NotNull
    fun provideGoogleApiService(@ApplicationContext appContext: Context): GoogleDirectionsApiService {
        return provideRetrofit(Constants.BASE_ENDPOINT_GOOGLE_DIRECTIONS_API, appContext)
            .create(GoogleDirectionsApiService::class.java)
    }
}