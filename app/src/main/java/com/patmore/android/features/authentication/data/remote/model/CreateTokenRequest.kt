package com.patmore.android.features.authentication.data.remote.model

import com.squareup.moshi.Json

data class CreateTokenRequest(
    @field:Json(name = "id") val id: String
)
