package com.android.patmore.features.foryou.presentation.model

import android.view.View
import coil.load
import com.android.patmore.R
import com.android.patmore.databinding.SingleCategoryTweetItemBinding
import com.android.patmore.features.media.GifMediaPresentation
import com.android.patmore.features.media.ImageMediaPresentation
import com.android.patmore.features.media.VideoMediaPresentation
import com.xwray.groupie.databinding.BindableItem

class SingleCategoryTweetItem(
    private val forYouTweetPresentation: ForYouTweetPresentation,
    val onClick: (ForYouTweetPresentation) -> Unit,
) :
    BindableItem<SingleCategoryTweetItemBinding>() {
    override fun bind(binding: SingleCategoryTweetItemBinding, p1: Int) {
        binding.categoryTweetText.text = forYouTweetPresentation.text
        binding.categoryTweetTextTime.text = forYouTweetPresentation.created

        binding.root.setOnClickListener {
            onClick(forYouTweetPresentation)
        }

        forYouTweetPresentation.mediaList?.let {
            if (it.isNotEmpty()) {
                val first = it[0]
                when (first) {
                    is ImageMediaPresentation -> {
                        val mediaUrl = first.mediaUrl
                        if (mediaUrl != null) {
                            // TODO: Use image loader
                            binding.categoryImageView.load(mediaUrl)
                        }
                    }
                    is VideoMediaPresentation -> {

                        binding.playImageView.visibility = View.VISIBLE
                        binding.categoryImageView.load(first.mediaPreviewUrl)
                    }
                    is GifMediaPresentation -> {
                        binding.gifPlayButton.visibility = View.VISIBLE
                        binding.playImageView.visibility = View.VISIBLE
                        binding.categoryImageView.load(first.mediaPreviewUrl)
                    }
                }
            }
        }
    }

    override fun getLayout(): Int = R.layout.single_category_tweet_item
}
