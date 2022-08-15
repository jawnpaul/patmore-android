package com.android.patmore.features.custom.presentation.state

import com.android.patmore.features.custom.presentation.model.SingleTweetPresentation

data class CustomTimelineView(
    val loading: Boolean = false,
    val response: List<SingleTweetPresentation>? = null,
    val error: String? = null
)
