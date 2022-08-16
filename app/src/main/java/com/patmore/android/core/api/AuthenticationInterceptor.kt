package com.patmore.android.core.api

import com.patmore.android.core.utility.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(private val sharedPreferences: SharedPreferences) : Interceptor {

    companion object {
        const val UNAUTHORIZED = 401
        const val TOKEN_TYPE = "Bearer "
        const val AUTH_HEADER = "Authorization"
        const val NO_AUTH_HEADER = "No Auth Header"
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPreferences.getAccessToken()

        val request = chain.request()

        return if (token != null) {
            // If token is not null, create authenticated request
            val interceptedRequest: Request = chain.createAuthenticatedRequest(token)
            chain.proceed(interceptedRequest)
        } else {
            chain.proceed(request)
        }
    }

    private fun Interceptor.Chain.createAuthenticatedRequest(token: String): Request {
        return request()
            .newBuilder()
            .addHeader(AUTH_HEADER, TOKEN_TYPE + token)
            .build()
    }
}
