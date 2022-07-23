package com.android.patmore.features.subscription.data.remote.model

import com.squareup.moshi.Json

data class CreateSubscriptionResponse(
    @field:Json(name = "message") val message: String
)
