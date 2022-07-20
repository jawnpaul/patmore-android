package com.android.patmore.core.di

import com.android.patmore.BuildConfig
import com.android.patmore.core.api.PatmoreApiService
import com.android.patmore.core.api.TwitterApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun providePatmoreApi(@Named("Patmore") builder: Retrofit.Builder): PatmoreApiService {
        return builder
            .build()
            .create(PatmoreApiService::class.java)
    }

    @Singleton
    @Provides
    @Named("Patmore")
    fun providePatmoreRetrofit(okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.PATMORE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
    }

    @Provides
    fun provideTwitterApi(@Named("Twitter") builder: Retrofit.Builder): TwitterApiService {
        return builder
            .build()
            .create(TwitterApiService::class.java)
    }

    @Singleton
    @Provides
    @Named("Twitter")
    fun provideTwitterRetrofit(okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.TWITTER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        // TODO:Remove this token
        val token = "UXV4aXVBWWNBUk5ka3hhUnFtSzFiREtLT3hJQVVES3h0R1VrS2Z6N1c3cXpFOjE2NTgzNDM1OTk1NTM6MTowOmF0OjE"
        if (BuildConfig.DEBUG) {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    chain.proceed(newRequest)
                }
                .build()
        }
        return OkHttpClient.Builder()
            .build()
    }
}
