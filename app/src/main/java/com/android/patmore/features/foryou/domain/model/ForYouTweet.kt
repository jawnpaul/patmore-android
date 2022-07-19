package com.android.patmore.features.foryou.domain.model

import com.android.patmore.features.foryou.presentation.model.ForYouTweetPresentation

data class ForYouTweet(
    val id: String,
    val text: String,
    val tweetMedia: List<TweetMedia>? = null,
) {
    fun toPresentation() = ForYouTweetPresentation(
        text = text,
        id = id,
        mediaList = tweetMedia?.map { it.toPresentation() }
    )
}
