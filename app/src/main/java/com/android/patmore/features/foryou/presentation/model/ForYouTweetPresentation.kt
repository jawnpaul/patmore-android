package com.android.patmore.features.foryou.presentation.model

import android.os.Parcelable
import com.android.patmore.features.foryou.data.remote.model.TweetAuthor
import com.android.patmore.features.media.TweetMediaPresentation
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForYouTweetPresentation(
    val text: String,
    val id: String,
    val created: String,
    val mediaList: List<TweetMediaPresentation>? = null,
    val category: String? = null,
    val tweetAuthor: TweetAuthor? = null
) : Parcelable
