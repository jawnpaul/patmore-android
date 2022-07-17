package com.android.patmore.core.imageloader

import android.widget.ImageView

interface IImageLoader {
    fun loadImage(url: String, imageView: ImageView)
}
