package com.android.patmore.features.foryou.presentation.state

import com.android.patmore.features.foryou.presentation.model.ForYouTweetPresentation

data class SingleTweetView(
    val isShown: Boolean = false,
    val data: ForYouTweetPresentation? = null
)
