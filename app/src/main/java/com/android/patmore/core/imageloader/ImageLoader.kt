package com.android.patmore.core.imageloader

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageLoader @Inject constructor(@ApplicationContext val context: Context) : IImageLoader {
    override fun loadImage(url: String, imageView: ImageView) {
        Glide.with(context).load(url).into(imageView)
    }
}
