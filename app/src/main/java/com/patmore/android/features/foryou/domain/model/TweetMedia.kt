package com.patmore.android.features.foryou.domain.model

import com.patmore.android.features.media.GifMediaPresentation
import com.patmore.android.features.media.ImageMediaPresentation
import com.patmore.android.features.media.TweetMediaPresentation
import com.patmore.android.features.media.VideoMediaPresentation

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
