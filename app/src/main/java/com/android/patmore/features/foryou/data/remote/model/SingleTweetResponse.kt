package com.android.patmore.features.foryou.data.remote.model

import com.squareup.moshi.Json

data class SingleTweetResponse(
    @field:Json(name = "data") val data: List<Res>,
    @field:Json(name = "includes") var includes: TweetIncludes? = null,
)

data class Res(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "text") val text: String,
    @field:Json(name = "created_at") val created: String,
    @field:Json(name = "attachments") val attachment: TweetAttachment? = null,
)

data class TweetAttachment(
    @field:Json(name = "media_keys") val mediaKeys: List<String>? = null,
)

data class TweetIncludes(
    @field:Json(name = "media") val media: List<TweetMedia>? = null,
)

data class TweetMedia(
    @field:Json(name = "media_key") var mediaKey: String,
    @field:Json(name = "type") var type: String,
    @field:Json(name = "url") var url: String,
    @field:Json(name = "preview_url") var previewUrl: String,
) {
    fun toDomain() =
        com.android.patmore.features.foryou.domain.model.TweetMedia(mediaKey, type, url, previewUrl)
}
