package com.patmore.android.features.authentication.data.remote.model

import com.squareup.moshi.Json

data class CreateTokenResponse(
    @field:Json(name = "message") val message: String,
    @field:Json(name = "data") val token: String
)
