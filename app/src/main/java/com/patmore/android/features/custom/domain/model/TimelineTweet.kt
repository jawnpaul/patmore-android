package com.patmore.android.features.custom.domain.model

import com.patmore.android.core.utility.toMillis
import com.patmore.android.core.utility.toRelativeTime
import com.patmore.android.features.custom.presentation.model.SingleTweetPresentation
import com.patmore.android.features.foryou.data.remote.model.TweetAuthor
import com.patmore.android.features.foryou.domain.model.TweetMedia

data class TimelineTweet(
    val id: String,
    val text: String,
    val created: String,
    val tweetMedia: List<TweetMedia>? = null,
    val tweetAuthor: TweetAuthor? = null
) {
    fun toPresentation() = SingleTweetPresentation(
        text = text,
        id = id,
        created = created.toMillis().toRelativeTime(),
        mediaList = tweetMedia?.map { it.toPresentation() },
        tweetAuthor = tweetAuthor
    )
}
