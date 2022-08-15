package com.android.patmore.features.foryou.domain.model

import com.android.patmore.features.media.GifMediaPresentation
import com.android.patmore.features.media.ImageMediaPresentation
import com.android.patmore.features.media.TweetMediaPresentation
import com.android.patmore.features.media.VideoMediaPresentation

data class TweetMedia(
    val mediaKey: String,
    val mediaType: String? = null,
    val mediaUrl: String? = null,
    val mediaPreviewUrl: String? = null,
) {
    fun toPresentation(): TweetMediaPresentation {
        return when (mediaType) {
            "photo" -> ImageMediaPresentation(mediaKey, mediaType, mediaUrl)
            "video" -> VideoMediaPresentation(mediaKey, mediaType, mediaUrl, mediaPreviewUrl)
            "animated_gif" -> GifMediaPresentation(mediaKey, mediaType, mediaUrl, mediaPreviewUrl)
            else -> {
                ImageMediaPresentation(mediaKey, "", "")
            }
        }
    }
}
