package com.patmore.android.features.foryou.data.remote.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

data class SingleTweetResponse(
    @field:Json(name = "data") val data: List<Res>,
    @field:Json(name = "includes") var includes: TweetIncludes? = null,
)

data class Res(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "text") val text: String,
    @field:Json(name = "created_at") val created: String,
    @field:Json(name = "author_id") val authorId: String,
    @field:Json(name = "attachments") val attachment: TweetAttachment? = null,
)

data class TweetAttachment(
    @field:Json(name = "media_keys") val mediaKeys: List<String>? = null,
)

data class TweetIncludes(
    @field:Json(name = "media") val media: List<TweetMedia>? = null,
    @field:Json(name = "users") val tweetAuthors: List<TweetAuthor>,
)

data class TweetMedia(
    @field:Json(name = "media_key") var mediaKey: String,
    @field:Json(name = "type") var type: String,
    @field:Json(name = "url") var url: String,
    @field:Json(name = "preview_image_url") var previewUrl: String?,
) {
    fun toDomain() =
        com.patmore.android.features.foryou.domain.model.TweetMedia(mediaKey, type, url, previewUrl)
}

@Parcelize
data class TweetAuthor(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "id") val id: String,
    @field:Json(name = "username") val userName: String,
    @field:Json(name = "profile_image_url") val profileImage: String,
) : Parcelable

data class UserTimelineResponse(
    @field:Json(name = "data") val data: List<Res>,
    @field:Json(name = "includes") var includes: TweetIncludes? = null,
    @field:Json(name = "meta") val meta: TimelineMeta,
)

data class TimelineMeta(
    @field:Json(name = "next_token") val nextToken: String,
    @field:Json(name = "result_count") val resultCount: String,
    @field:Json(name = "newest_id") val newestId: String,
    @field:Json(name = "oldest_id") val oldestId: String,
)
