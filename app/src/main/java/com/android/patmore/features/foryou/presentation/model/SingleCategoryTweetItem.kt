package com.android.patmore.features.foryou.presentation.model

import coil.load
import com.android.patmore.R
import com.android.patmore.databinding.SingleCategoryTweetItemBinding
import com.xwray.groupie.databinding.BindableItem

class SingleCategoryTweetItem(val forYouTweetPresentation: ForYouTweetPresentation) : BindableItem<SingleCategoryTweetItemBinding>() {
    override fun bind(binding: SingleCategoryTweetItemBinding, p1: Int) {
        binding.categoryTweetText.text = forYouTweetPresentation.text
        binding.categoryTweetTextTime.text = forYouTweetPresentation.created

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
                    }
                }
            }
        }
    }

    override fun getLayout(): Int = R.layout.single_category_tweet_item
}
