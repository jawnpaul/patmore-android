package com.patmore.android.features.foryou.data.remote.model

import com.squareup.moshi.Json

data class TweetCategoryResponse(
    @field:Json(name = "total") val total: Int,
    @field:Json(name = "currentPage") val currentPage: Int,
    @field:Json(name = "totalPages") val totalPages: Int,
    @field:Json(name = "data") val response: List<TweetResponse>,
)

data class TagResponse(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "tag") val tag: String,
)

data class TweetResponse(
    @field:Json(name = "matching_rule") val matchingRule: TagResponse,
    @field:Json(name = "_id") val id: String,
    @field:Json(name = "id") val tweetId: String,
    @field:Json(name = "createdAt") val createdAt: String,
    @field:Json(name = "updatedAt") val updatedAt: String,
    @field:Json(name = "text") val text: String,
)

data class ForYouResponse(
    @field:Json(name = "message") val message: String,
    @field:Json(name = "data") val data: ForYouData
)

data class ForYouData(

    @field:Json(name = "anime") val anime: List<TweetResponse>? = null,
    @field:Json(name = "sport") val sport: List<TweetResponse>? = null,
    @field:Json(name = "business") val business: List<TweetResponse>? = null,
    @field:Json(name = "technology") val technology: List<TweetResponse>? = null,
    @field:Json(name = "music") val music: List<TweetResponse>? = null,
    @field:Json(name = "travel") val travel: List<TweetResponse>? = null,
)
