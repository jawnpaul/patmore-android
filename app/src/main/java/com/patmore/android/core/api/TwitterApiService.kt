package com.patmore.android.core.api

import com.patmore.android.features.authentication.data.remote.oauth.OAuth2PCKEResponse
import com.patmore.android.features.custom.data.remote.SingleTwitterUser
import com.patmore.android.features.custom.data.remote.UserFollowerResponse
import com.patmore.android.features.foryou.data.remote.model.SingleTweetResponse
import com.patmore.android.features.foryou.data.remote.model.UserTimelineResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
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
        @Query("exclude") excludes: String,
    ): Response<UserTimelineResponse>

    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST("oauth2/token")
    suspend fun refreshToken(
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
    ): Response<OAuth2PCKEResponse>

    @GET("users/{id}/followers")
    suspend fun getUserFollowing(
        @Path("id") userId: String,
        @Query("user.fields") userFields: String,
        @Query("pagination_token") paginationToken: String?,
        @Query("max_results") maxResults: Int,
    ): Response<UserFollowerResponse>

    @POST("oauth2/token")
    suspend fun getAccessToken(
        @Query("code") code: String,
        @Query("grant_type") grantType: String,
        @Query("client_id") clientId: String,
        @Query("redirect_uri") redirectUrl: String,
        @Query("code_verifier") challenge: String,
    ): Response<OAuth2PCKEResponse>
}
