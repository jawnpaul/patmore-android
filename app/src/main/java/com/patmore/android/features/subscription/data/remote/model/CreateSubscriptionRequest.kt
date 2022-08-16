package com.patmore.android.features.subscription.data.remote.model

import com.squareup.moshi.Json

data class CreateSubscriptionRequest(
    @field:Json(name = "categories")val categories: List<String>
)
