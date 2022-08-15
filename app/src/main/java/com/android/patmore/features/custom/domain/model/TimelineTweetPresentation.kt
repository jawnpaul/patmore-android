package com.android.patmore.features.custom.domain.model

import com.android.patmore.features.foryou.data.remote.model.TweetAuthor
import com.android.patmore.features.media.TweetMediaPresentation

data class TimelineTweetPresentation(
    val text: String,
    val id: String,
    val created: String,
    val mediaList: List<TweetMediaPresentation>? = null,
    val category: String? = null,
    val tweetAuthor: TweetAuthor? = null,
)
