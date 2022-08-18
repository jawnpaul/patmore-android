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

    @field:Json(name = "food") val food: List<TweetResponse>? = null,
    @field:Json(name = "music") val music: List<TweetResponse>? = null,
    @field:Json(name = "travel") val travel: List<TweetResponse>? = null,
    @field:Json(name = "sports") val sports: List<TweetResponse>? = null,
    @field:Json(name = "careers") val careers: List<TweetResponse>? = null,
    @field:Json(name = "fitness") val fitness: List<TweetResponse>? = null,
    @field:Json(name = "science") val science: List<TweetResponse>? = null,
    @field:Json(name = "business") val business: List<TweetResponse>? = null,
    @field:Json(name = "technology") val technology: List<TweetResponse>? = null,
    @field:Json(name = "arts and culture") val artsAndCulture: List<TweetResponse>? = null,
    @field:Json(name = "fashion and beauty") val fashionAndBeauty: List<TweetResponse>? = null,
    @field:Json(name = "animation and comics") val anime: List<TweetResponse>? = null,
    @field:Json(name = "family and relationships") val familyAndRelationships: List<TweetResponse>? = null,
)
