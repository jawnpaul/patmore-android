package com.patmore.android.features.authentication.data.remote.oauth

data class OAuth2PCKEResponse(

    /**
     * Bearer token to send in requests
     */
    val access_token: String,
    /**
     * Time in seconds
     */
    val expires_in: Int,
    /**
     * Refresh token to get a new access_token when it has expired
     * Only returned when using scope offline.access
     */
    val refresh_token: String? = null,
    /**
     * All the granted scopes for the token
     */
    val scope: String,
    val token_type: String,
)
