package com.android.patmore.features.foryou.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForYouTweetPresentation(
    val text: String,
    val id: String,
    val created: String,
    val mediaList: List<TweetMediaPresentation>? = null,
    val category: String? = null
) : Parcelable

@Parcelize
sealed class TweetMediaPresentation : Parcelable

data class ImageMediaPresentation(
    val mediaKey: String,
    val mediaType: String,
    val mediaUrl: String? = null
) : TweetMediaPresentation()

data class VideoMediaPresentation(
    val mediaKey: String,
    val mediaType: String,
    val mediaUrl: String? = null,
    val mediaPreviewUrl: String? = null
) : TweetMediaPresentation()
