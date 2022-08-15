package com.android.patmore.core.api

import com.android.patmore.BuildConfig
import com.android.patmore.core.utility.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class TwitterInterceptor @Inject constructor(private val sharedPreferences: SharedPreferences) :
    Interceptor {

    companion object {
        const val UNAUTHORIZED = 401
        const val TOKEN_TYPE = "Bearer "
        const val AUTH_HEADER = "Authorization"
        const val NO_AUTH_HEADER = "No Auth Header"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = if (sharedPreferences.getTwitterUserAccessToken() == null) {
            BuildConfig.TWITTER_TOKEN
        } else {
            // sharedPreferences.getTwitterUserAccessToken()!!
            BuildConfig.TWITTER_TOKEN
        }

        val interceptedRequest = chain.createAuthenticatedRequest(token)
        return chain.proceed(interceptedRequest)
    }

    private fun Interceptor.Chain.createAuthenticatedRequest(token: String): Request {
        return request()
            .newBuilder()
            .addHeader(AuthenticationInterceptor.AUTH_HEADER, TOKEN_TYPE + token)
            .build()
    }
}
