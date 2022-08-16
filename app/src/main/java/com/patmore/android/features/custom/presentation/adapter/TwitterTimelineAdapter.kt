package com.patmore.android.features.custom.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.patmore.android.core.imageloader.ImageLoader
import com.patmore.android.databinding.SingleTweetItemBinding
import com.patmore.android.features.custom.presentation.model.SingleTweetPresentation
import com.patmore.android.features.media.GifMediaPresentation
import com.patmore.android.features.media.ImageMediaPresentation
import com.patmore.android.features.media.TweetMediaPresentation
import com.patmore.android.features.media.VideoMediaPresentation

class TwitterTimelineAdapter(private val onclickTweet: (String) -> Unit) :
    ListAdapter<SingleTweetPresentation, TwitterTimelineAdapter.ViewHolder>(
        TimelineDiffCallback()
    ) {

    class ViewHolder constructor(private val binding: SingleTweetItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val imageLoader = ImageLoader(binding.root.context)
        fun bind(
            onclickTweet: (String) -> Unit,
            item: SingleTweetPresentation,
        ) {
            binding.singleTweetText.text = item.text

            item.tweetAuthor?.let { tweetAuthor ->
                binding.userHandleTextView.text = tweetAuthor.userName
                binding.userNameTextView.text = tweetAuthor.name
                imageLoader.loadCircleImage(tweetAuthor.profileImage, binding.profileImageView)
            }

            item.mediaList?.let { media ->
                renderMedia(binding.singleTweetMediaContainer, media)
            }

            binding.root.setOnClickListener {
                onclickTweet(item.id)
            }

            binding.executePendingBindings()
        }

        private fun renderMedia(
            mediaContainer: ViewGroup,
            media: List<TweetMediaPresentation>,
        ) {
            mediaContainer.visibility = View.VISIBLE

            when (media.size) {
                1 -> {
                    when (media[0]) {
                        is ImageMediaPresentation -> {
                            renderPhotos(mediaContainer, media as List<ImageMediaPresentation>)
                        }
                        is GifMediaPresentation -> {
                            val gif = media[0] as GifMediaPresentation
                            mediaContainer.run {
                                binding.mediaOne.mediaOne.visibility = View.GONE
                                binding.mediaTwo.twoMediaContainer.visibility = View.GONE
                                binding.mediaThree.threeMediaContainer.visibility = View.GONE
                                binding.mediaFour.fourMediaContainer.visibility = View.GONE
                                binding.mediaVideo.videoContainer.visibility = View.GONE
                                binding.mediaGif.gifContainer.visibility = View.VISIBLE
                                imageLoader.loadImage(
                                    "${gif.mediaPreviewUrl}:small",
                                    binding.mediaGif.gifImage
                                )

                                /*gifContainer.setOnClickListener {
                                    onMediaClickListener.onVideoOrGifClick(media[0].url)
                                }*/
                            }
                        }
                        is VideoMediaPresentation -> {
                            val video = media[0] as VideoMediaPresentation
                            mediaContainer.run {
                                binding.mediaOne.mediaOne.visibility = View.GONE
                                binding.mediaTwo.twoMediaContainer.visibility = View.GONE
                                binding.mediaThree.threeMediaContainer.visibility = View.GONE
                                binding.mediaFour.fourMediaContainer.visibility = View.GONE
                                binding.mediaVideo.videoContainer.visibility = View.VISIBLE
                                binding.mediaGif.gifContainer.visibility = View.GONE
                                imageLoader.loadImage(
                                    "${video.mediaPreviewUrl}:small",
                                    binding.mediaVideo.videoImage
                                )

                                /*videoContainer.setOnClickListener {
                                    onMediaClickListener.onVideoOrGifClick(media[0].url)
                                }*/
                            }
                        }
                    }
                }
                else -> {
                    renderPhotos(mediaContainer, media as List<ImageMediaPresentation>)
                }
            }
        }

        private fun renderPhotos(mediaContainer: ViewGroup, media: List<ImageMediaPresentation>) {
            mediaContainer.apply {
                binding.mediaOne.mediaOne.visibility =
                    if (media.size == 1) View.VISIBLE else View.GONE
                binding.mediaTwo.twoMediaContainer.visibility =
                    if (media.size == 2) View.VISIBLE else View.GONE
                binding.mediaThree.threeMediaContainer.visibility =
                    if (media.size == 3) View.VISIBLE else View.GONE
                binding.mediaFour.fourMediaContainer.visibility =
                    if (media.size == 4) View.VISIBLE else View.GONE
                binding.mediaVideo.videoContainer.visibility = View.GONE
                binding.mediaGif.gifContainer.visibility = View.GONE
                when (media.size) {
                    1 -> {
                        imageLoader.loadImage(
                            "${media[0].mediaUrl}:small",
                            binding.mediaOne.mediaOne
                        )
                    }
                    2 -> {
                        imageLoader.loadImage(
                            "${media[0].mediaUrl}:small",
                            binding.mediaTwo.twoImageMediaOne
                        )

                        imageLoader.loadImage(
                            "${media[1].mediaUrl}:small",
                            binding.mediaTwo.twoImageMediaTwo
                        )
                    }
                    3 -> {
                        imageLoader.loadImage(
                            "${media[0].mediaUrl}:small",
                            binding.mediaThree.threeImageMediaOne
                        )

                        imageLoader.loadImage(
                            "${media[1].mediaUrl}:small",
                            binding.mediaThree.threeImageMediaTwo
                        )

                        imageLoader.loadImage(
                            "${media[2].mediaUrl}:small",
                            binding.mediaThree.threeImageMediaThree
                        )
                    }
                    4 -> {
                        imageLoader.loadImage(
                            "${media[0].mediaUrl}:small",
                            binding.mediaFour.fourImageMediaOne
                        )

                        imageLoader.loadImage(
                            "${media[1].mediaUrl}:small",
                            binding.mediaFour.fourImageMediaTwo
                        )

                        imageLoader.loadImage(
                            "${media[2].mediaUrl}:small",
                            binding.mediaFour.fourImageMediaThree
                        )

                        imageLoader.loadImage(
                            "${media[3].mediaUrl}:small",
                            binding.mediaFour.fourImageMediaFour
                        )
                    }
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SingleTweetItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(onclickTweet, item)
    }
}

class TimelineDiffCallback :
    DiffUtil.ItemCallback<SingleTweetPresentation>() {
    override fun areItemsTheSame(
        oldItem: SingleTweetPresentation,
        newItem: SingleTweetPresentation,
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: SingleTweetPresentation,
        newItem: SingleTweetPresentation,
    ): Boolean {
        return oldItem == newItem
    }
}
