package com.patmore.android.features.foryou.presentation.state

import com.patmore.android.features.foryou.presentation.model.ForYouTweetPresentation

data class CategoryTweetView(
    val isShown: Boolean = false,
    val data: List<ForYouTweetPresentation>? = null
)
