package com.patmore.android.features.authentication.data.remote.model

data class AccessTokenRequest(
    val code: String,
    val challenge: String,
    val clientID: String,
    val callback: String
)
