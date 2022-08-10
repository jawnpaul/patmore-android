package com.android.patmore.features.foryou.presentation.state

import com.android.patmore.features.foryou.presentation.model.ForYouTweetPresentation

data class CategoryTweetView(
    val isShown: Boolean = false,
    val data: List<ForYouTweetPresentation>? = null
)
