package com.android.patmore.core.api

import com.android.patmore.features.foryou.data.remote.model.TweetCategoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PatmoreApiService {

    @GET("tweets")
    suspend fun getCategory(
        @Query("category") category: String
    ): Response<TweetCategoryResponse>
}
