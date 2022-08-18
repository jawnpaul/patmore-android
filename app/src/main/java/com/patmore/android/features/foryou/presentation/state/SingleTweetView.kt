package com.patmore.android.features.foryou.presentation.state

import com.patmore.android.features.foryou.presentation.model.ForYouTweetPresentation

data class SingleTweetView(
    val isShown: Boolean = false,
    val data: ForYouTweetPresentation? = null
)
