package com.patmore.android.features.custom.presentation.state

import com.patmore.android.features.custom.presentation.model.SingleTweetPresentation

data class CustomTimelineView(
    val loading: Boolean = false,
    val response: List<SingleTweetPresentation>? = null,
    val error: String? = null
)
