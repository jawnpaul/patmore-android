package com.android.patmore.features.subscription.presentation.state

data class CreateSubscriptionView(
    val loading: Boolean = false,
    val error: String? = null,
    val response: String? = null,
)
