package com.patmore.android.core.di

import com.patmore.android.BuildConfig
import com.patmore.android.core.api.AuthenticationInterceptor
import com.patmore.android.core.api.PatmoreApiService
import com.patmore.android.core.api.TokenAuthenticator
import com.patmore.android.core.api.TwitterApiService
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
    fun provideTwitterRetrofit(tokenAuthenticator: TokenAuthenticator): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.TWITTER_BASE_URL)
            .client(provideTwitterOkHttPClient(tokenAuthenticator))
            .addConverterFactory(MoshiConverterFactory.create())
    }

    @Provides
    fun providePatmoreOkHttPClient(authenticationInterceptor: AuthenticationInterceptor): OkHttpClient {
        if (BuildConfig.DEBUG) {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authenticationInterceptor)
                .build()
        }
        return OkHttpClient.Builder()
            .addInterceptor(authenticationInterceptor)
            .build()
    }

    private fun provideTwitterOkHttPClient(tokenAuthenticator: TokenAuthenticator): OkHttpClient {
        if (BuildConfig.DEBUG) {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            return OkHttpClient.Builder()
                .authenticator(tokenAuthenticator)
                .addInterceptor(loggingInterceptor)
                .build()
        }
        return OkHttpClient.Builder()
            .authenticator(tokenAuthenticator)
            .build()
    }
}
