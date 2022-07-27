package com.android.patmore.features.foryou.domain.model

import com.android.patmore.core.utility.toMillis
import com.android.patmore.core.utility.toRelativeTime
import com.android.patmore.features.foryou.data.remote.model.TweetAuthor
import com.android.patmore.features.foryou.presentation.model.ForYouTweetPresentation

data class ForYouTweet(
    val id: String,
    val text: String,
    val created: String,
    val tweetMedia: List<TweetMedia>? = null,
    val tweetAuthor: TweetAuthor? = null
) {
    fun toPresentation() = ForYouTweetPresentation(
        text = text,
        id = id,
        created = created.toMillis().toRelativeTime(),
        mediaList = tweetMedia?.map { it.toPresentation() },
        tweetAuthor = tweetAuthor
    )
}
