package com.android.patmore.features.authentication.data.remote.model

data class Oauth1AccessTokenRequest(
    val oAuthToken: String,
    val oAuthVerifier: String
)
