package com.android.patmore.core.imageloader

import android.content.Context
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageLoader @Inject constructor(@ApplicationContext val context: Context) : IImageLoader {
    override fun loadImage(url: String, imageView: ImageView) {
        Glide.with(context).load(url).into(imageView)
    }

    override fun loadCircleImage(url: String, imageView: ImageView) {
        imageView.load(url) {
            transformations(CircleCropTransformation())
        }
    }

    override fun getPalette(url: String, palette: (Palette?) -> Unit) {
        val imageLoader = context.imageLoader
        val request = ImageRequest.Builder(context)
            .allowHardware(false)
            .data(url)
            .target { drawable ->
                Palette.Builder(drawable.toBitmap()).generate {
                    palette(it)
                }
            }
            .build()
        imageLoader.enqueue(request)
    }
}
