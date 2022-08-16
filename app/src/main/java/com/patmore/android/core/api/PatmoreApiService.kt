package com.patmore.android.core.api

import com.patmore.android.features.authentication.data.remote.model.CreateTokenRequest
import com.patmore.android.features.authentication.data.remote.model.CreateTokenResponse
import com.patmore.android.features.foryou.data.remote.model.ForYouResponse
import com.patmore.android.features.foryou.data.remote.model.TweetCategoryResponse
import com.patmore.android.features.subscription.data.remote.model.CreateSubscriptionRequest
import com.patmore.android.features.subscription.data.remote.model.CreateSubscriptionResponse
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

    @GET("top")
    suspend fun getUserSubscriptionTweets(
        @Query("search") category: String,
        @Query("page") page: Int,
    ): Response<ForYouResponse>

    @POST("token")
    suspend fun generateToken(@Body request: CreateTokenRequest): Response<CreateTokenResponse>

    @POST("subscriptions")
    suspend fun subscribeUser(@Body request: CreateSubscriptionRequest): Response<CreateSubscriptionResponse>
}
