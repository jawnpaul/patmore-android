package com.android.patmore.features.custom.domain.model

import com.android.patmore.features.foryou.data.remote.model.TweetAuthor
import com.android.patmore.features.foryou.domain.model.TweetMedia

data class TimelineTweet(
    val id: String,
    val text: String,
    val created: String,
    val tweetMedia: List<TweetMedia>? = null,
    val tweetAuthor: TweetAuthor? = null
)
