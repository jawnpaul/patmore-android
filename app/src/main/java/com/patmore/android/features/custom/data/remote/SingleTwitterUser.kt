package com.patmore.android.features.custom.data.remote

import com.patmore.android.features.foryou.data.remote.model.TweetAuthor
import com.squareup.moshi.Json

data class SingleTwitterUser(
    @field:Json(name = "data") val user: TweetAuthor,
)
