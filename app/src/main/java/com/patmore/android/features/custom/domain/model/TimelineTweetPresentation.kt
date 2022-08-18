package com.patmore.android.features.custom.domain.model

import com.patmore.android.features.foryou.data.remote.model.TweetAuthor
import com.patmore.android.features.media.TweetMediaPresentation

data class TimelineTweetPresentation(
    val text: String,
    val id: String,
    val created: String,
    val mediaList: List<TweetMediaPresentation>? = null,
    val category: String? = null,
    val tweetAuthor: TweetAuthor? = null,
)
