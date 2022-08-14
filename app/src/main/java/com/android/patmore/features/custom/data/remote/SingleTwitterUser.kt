package com.android.patmore.features.custom.data.remote

import com.android.patmore.features.foryou.data.remote.model.TweetAuthor
import com.squareup.moshi.Json

data class SingleTwitterUser(
    @field:Json(name = "data") val user: TweetAuthor,
)
