package com.android.patmore.core.api

import com.android.patmore.features.foryou.data.remote.model.SingleTweetResponse
import com.android.patmore.features.foryou.data.remote.model.TweetAuthor
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CustomTwitterApiService {
    @GET("users/me")
    suspend fun getCurrentUser(@Query("user.fields") userFields: String): Response<TweetAuthor>

    @GET("users")
    suspend fun getUserHomeTimeline(
        @Path("id") userId: String,
        @Query("expansions") expansions: String,
        @Query("media.fields") mediaFields: String,
        @Query("tweet.fields") fields: String,
        @Query("user.fields") userFields: String,
    ): Response<SingleTweetResponse>
}
