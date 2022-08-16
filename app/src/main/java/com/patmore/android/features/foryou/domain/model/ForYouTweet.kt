package com.patmore.android.features.foryou.domain.model

import com.patmore.android.core.utility.toMillis
import com.patmore.android.core.utility.toRelativeTime
import com.patmore.android.features.foryou.data.remote.model.TweetAuthor
import com.patmore.android.features.foryou.presentation.model.ForYouTweetPresentation

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
