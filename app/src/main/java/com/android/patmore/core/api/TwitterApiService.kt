package com.android.patmore.core.api

import com.android.patmore.features.custom.data.remote.SingleTwitterUser
import com.android.patmore.features.foryou.data.remote.model.SingleTweetResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TwitterApiService {
    @GET("tweets")
    suspend fun getTweet(
        @Query("ids") id: String,
        @Query("expansions") expansions: String,
        @Query("media.fields") mediaFields: String,
        @Query("tweet.fields") fields: String,
        @Query("user.fields") userFields: String,
    ): Response<SingleTweetResponse>

    @GET("users/me")
    suspend fun getCurrentUser(@Query("user.fields") userFields: String): Response<SingleTwitterUser>

    @GET("users/{id}/timelines/reverse_chronological")
    suspend fun getUserHomeTimeline(
        @Path("id") userId: String,
        @Query("expansions") expansions: String,
        @Query("media.fields") mediaFields: String,
        @Query("tweet.fields") fields: String,
        @Query("user.fields") userFields: String,
    ): Response<SingleTweetResponse>
}
