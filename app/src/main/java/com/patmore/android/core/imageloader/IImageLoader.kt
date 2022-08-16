package com.patmore.android.core.imageloader

import android.widget.ImageView
import androidx.palette.graphics.Palette

interface IImageLoader {
    fun loadImage(url: String, imageView: ImageView)

    fun loadCircleImage(url: String, imageView: ImageView)

    fun getPalette(url: String, palette: (Palette?) -> Unit)
}
