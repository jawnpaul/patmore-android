package com.android.patmore.core.api

import com.android.patmore.features.authentication.data.remote.model.CreateTokenRequest
import com.android.patmore.features.authentication.data.remote.model.CreateTokenResponse
import com.android.patmore.features.foryou.data.remote.model.TweetCategoryResponse
import com.android.patmore.features.subscription.data.remote.model.CreateSubscriptionRequest
import com.android.patmore.features.subscription.data.remote.model.CreateSubscriptionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PatmoreApiService {

    @GET("tweets")
    suspend fun getCategory(
        @Query("category") category: String,
    ): Response<TweetCategoryResponse>

    @POST("token")
    suspend fun generateToken(@Body request: CreateTokenRequest): Response<CreateTokenResponse>

    @POST("subscriptions")
    suspend fun subscribeUser(@Body request: CreateSubscriptionRequest): Response<CreateSubscriptionResponse>
}
