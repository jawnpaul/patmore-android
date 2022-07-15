package com.android.patmore.core.di

import com.android.patmore.BuildConfig
import com.android.patmore.core.api.PatmoreApiService
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
    fun provideTwitterApi(@Named("Twitter") builder: Retrofit.Builder): PatmoreApiService {
        return builder
            .build()
            .create(PatmoreApiService::class.java)
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
        if (BuildConfig.DEBUG) {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        }
        return OkHttpClient.Builder()
            .build()
    }
}
