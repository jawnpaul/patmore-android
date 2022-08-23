package com.patmore.android.core.api

import com.patmore.android.BuildConfig
import com.patmore.android.core.utility.SharedPreferences
import com.patmore.android.features.authentication.data.remote.oauth.OAuth2PCKEResponse
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Inject

class TokenAuthenticator @Inject constructor() :
    Authenticator {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun authenticate(route: Route?, response: Response): Request? {
        val requestAvailable: Request? = null

        try {
            return runBlocking {

                if (sharedPreferences.getTwitterUserAccessToken() == null) {
                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${BuildConfig.TWITTER_TOKEN}")
                        .build()
                } else {
                    // 1. Refresh your access_token using a synchronous api request
                    val rr = getToken()

                    val accessToken = rr?.access_token
                    val refresh = rr?.refresh_token
                    sharedPreferences.saveTwitterUserAccessToken(accessToken!!)
                    refresh?.let { it1 -> sharedPreferences.saveTwitterUserRefreshToken(it1) }

                    val token = if (sharedPreferences.getTwitterUserAccessToken() == null) {
                        BuildConfig.TWITTER_TOKEN
                    } else {
                        sharedPreferences.getTwitterUserAccessToken()!!
                    }

                    response.request.newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                }
            }
        } catch (e: Exception) {
            Timber.e(e.stackTraceToString())
        }

        return requestAvailable
    }

    private suspend fun getToken(): OAuth2PCKEResponse? {

        val refreshToken = sharedPreferences.getTwitterUserRefreshToken()
        if (refreshToken == null) {
            return null
        } else {

            val clientId = BuildConfig.TWITTER_CLIENT_ID
            val grantType = "refresh_token"

            val okHttpClient = OkHttpClient().newBuilder()
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.TWITTER_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            val service = retrofit.create(TwitterApiService::class.java)
            return service.refreshToken(refreshToken, grantType, clientId).body()
        }
    }
}
