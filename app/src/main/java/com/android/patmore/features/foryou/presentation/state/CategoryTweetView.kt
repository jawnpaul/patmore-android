package com.android.patmore.features.foryou.presentation.state

import com.android.patmore.features.foryou.presentation.view.CategoryFragment

data class CategoryTweetView(
    val isShown: Boolean = false,
    val data: List<CategoryFragment>? = null
)
