package com.patmore.android.features.foryou.presentation.model

import android.os.Parcelable
import com.patmore.android.features.foryou.data.remote.model.TweetAuthor
import com.patmore.android.features.media.TweetMediaPresentation
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
