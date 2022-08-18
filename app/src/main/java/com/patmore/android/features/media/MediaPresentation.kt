package com.patmore.android.features.media

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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

data class GifMediaPresentation(
    val mediaKey: String,
    val mediaType: String,
    val mediaUrl: String? = null,
    val mediaPreviewUrl: String? = null
) : TweetMediaPresentation()
