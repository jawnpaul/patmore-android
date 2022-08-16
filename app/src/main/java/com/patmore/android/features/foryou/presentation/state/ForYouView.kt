package com.patmore.android.features.foryou.presentation.state

import com.xwray.groupie.Section

data class ForYouView(
    val loading: Boolean = false,
    val response: List<Section>? = null,
    val error: String? = null
)
