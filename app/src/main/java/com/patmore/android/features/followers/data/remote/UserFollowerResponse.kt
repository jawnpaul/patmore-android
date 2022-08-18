package com.patmore.android.features.custom.data.remote

import com.patmore.android.features.foryou.data.remote.model.TweetAuthor
import com.squareup.moshi.Json

data class UserFollowerResponse(
    @field:Json(name = "data") val data: List<TweetAuthor>,
    @field:Json(name = "meta") val meta: FollowingMeta,
)

data class FollowingMeta(
    @field:Json(name = "result_count") val count: Int,
    @field:Json(name = "next_token") val nextToken: String?,
    @field:Json(name = "prev_token") val prevToken: String?,
)
